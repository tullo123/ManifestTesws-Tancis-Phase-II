package com.ManifestTeswTancis.ServiceImpl;

import com.ManifestTeswTancis.Entity.ExportManifest;
import com.ManifestTeswTancis.Entity.ManifestApprovalStatus;
import com.ManifestTeswTancis.Entity.QueueMessageStatusEntity;
import com.ManifestTeswTancis.RabbitConfigurations.*;
import com.ManifestTeswTancis.Repository.ExportManifestRepository;
import com.ManifestTeswTancis.Repository.ManifestApprovalStatusRepository;
import com.ManifestTeswTancis.Repository.QueueMessageStatusRepository;
import com.ManifestTeswTancis.Response.SubmittedExportManifestStatusNotice;
import com.ManifestTeswTancis.Util.ManifestStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Component
@Service
public class CheckReceivedExportManifestStatusImpl {
    private static final Logger LOGGER = LoggerFactory.getLogger(CheckReceivedExportManifestStatusImpl.class);
    @Value("${spring.rabbitmq.exchange.out}")
    private String OUTBOUND_EXCHANGE;
    final QueueMessageStatusRepository queueMessageStatusRepository;
    final MessageProducer rabbitMqMessageProducer;
    final ManifestApprovalStatusRepository statusRepository;
    final ExportManifestRepository exportManifestRepository;

    public CheckReceivedExportManifestStatusImpl(QueueMessageStatusRepository queueMessageStatusRepository, MessageProducer rabbitMqMessageProducer, ManifestApprovalStatusRepository statusRepository, ExportManifestRepository exportManifestRepository) {
        this.queueMessageStatusRepository = queueMessageStatusRepository;
        this.rabbitMqMessageProducer = rabbitMqMessageProducer;
        this.statusRepository = statusRepository;
        this.exportManifestRepository = exportManifestRepository;
    }
    @Transactional
    @Scheduled(fixedRate = 1500000)
    public void CheckReceivedExportManifest(){
        List<ManifestApprovalStatus> exportManifest = statusRepository.findByExportReceivedStatusFalse();
        for (ManifestApprovalStatus mf : exportManifest) {
            if (mf.isExportApprovedStatus()) {
                Optional<ExportManifest> optional = exportManifestRepository.findFirstByMrnOut(mf.getMrnOut());
                if (optional.isPresent()) {
                    ExportManifest export = optional.get();
                    if (ManifestStatus.RECEIVED.equalsIgnoreCase(export.getProcessingStatus())) {
                        LocalDateTime localDateTime = LocalDateTime.now();
                        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                        SubmittedExportManifestStatusNotice submittedStatus = new SubmittedExportManifestStatusNotice();
                        submittedStatus.setNoticeDate(localDateTime.format(format));
                        submittedStatus.setCommunicationAgreedId(export.getCommunicationAgreedId());
                        submittedStatus.setManifestReferenceNumber(export.getMrnOut());
                        submittedStatus.setVoyageNumber(export.getVoyageNumberOutbound());
                        submittedStatus.setCustomOfficeCode(export.getCustomOfficeCode());
                        submittedStatus.setStatus(getExportReceivedStatus(export.getProcessingStatus()));
                        mf.setExportReceivedStatus(true);
                        statusRepository.save(mf);
                        String response = sendExportManifestReceivedNoticeToQueue(submittedStatus);
                        LOGGER.info("[Export Manifest Received Notice]" + response);
                    }
                }

            }
        }
    }

    private String sendExportManifestReceivedNoticeToQueue(SubmittedExportManifestStatusNotice submittedStatus) {
        ObjectMapper mapper = new ObjectMapper();
        try{
            String payload = mapper.writeValueAsString(submittedStatus);
            LOGGER.info("[Export Manifest Status]" + payload);
            MessageDto messageDto = new MessageDto();
            SubmittedExportManifestStatusMessageDto submitted= new SubmittedExportManifestStatusMessageDto();
            submitted.setMessageName(MessageNames.SUBMITTED_EXPORT_MANIFEST_STATUS);
            RequestIdDto requestIdDto = mapper.readValue(getId(), RequestIdDto.class);
            submitted.setRequestId(requestIdDto.getMessageId());
            messageDto.setPayload(submittedStatus);
            AcknowledgementDto queueResponse = rabbitMqMessageProducer.
                    sendMessage(OUTBOUND_EXCHANGE, MessageNames.SUBMITTED_EXPORT_MANIFEST_STATUS, submitted.getRequestId(), messageDto.getCallbackUrl(), messageDto.getPayload());
            LOGGER.info("[payload Submitted To RabbitMQ]" + queueResponse);

            QueueMessageStatusEntity queueMessage = new QueueMessageStatusEntity();
            queueMessage.setMessageId(submittedStatus.getCommunicationAgreedId());
            queueMessage.setReferenceId(submitted.getRequestId());
            queueMessage.setMessageName(MessageNames.SUBMITTED_EXPORT_MANIFEST_STATUS);
            queueMessage.setProcessStatus("1");
            queueMessage.setProcessId("TANCIS-TESWS.API");
            queueMessage.setFirstRegistrationId("TANCIS-TESWS.API");
            queueMessage.setLastUpdateId("TANCIS-TESWS.API");
            LocalDateTime localDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            queueMessage.setProcessingDate(localDateTime.format(formatter));
            queueMessage.setFirstRegisterDate(localDateTime.format(formatter));
            queueMessage.setLastUpdateDate(localDateTime.format(formatter));
            queueMessageStatusRepository.save(queueMessage);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return "success";
    }


    private String getExportReceivedStatus(String processingStatus) {
        if(processingStatus.equalsIgnoreCase("B")){
            return "RECEIVED";
        }
        return processingStatus;
    }

    private String getId() throws IOException {
        String url = "http://192.168.30.200:7074/GetId";
        HttpGet request = new HttpGet(url);
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = client.execute(request);
        HttpEntity entity = response.getEntity();
        return EntityUtils.toString(entity);
    }
}
