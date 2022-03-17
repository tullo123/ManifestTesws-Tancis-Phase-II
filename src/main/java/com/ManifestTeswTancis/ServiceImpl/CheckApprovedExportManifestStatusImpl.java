package com.ManifestTeswTancis.ServiceImpl;

import com.ManifestTeswTancis.Entity.ExportManifest;
import com.ManifestTeswTancis.Entity.ManifestApprovalStatus;
import com.ManifestTeswTancis.Entity.QueueMessageStatusEntity;
import com.ManifestTeswTancis.RabbitConfigurations.*;
import com.ManifestTeswTancis.Repository.*;
import com.ManifestTeswTancis.Response.SubmittedExportManifestApprovalNotice;
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
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Component
@Service
public class CheckApprovedExportManifestStatusImpl {
    private static final Logger LOGGER = LoggerFactory.getLogger(CheckApprovedExportManifestStatusImpl.class);
    @Value("${spring.rabbitmq.exchange.out}")
    private String OUTBOUND_EXCHANGE;
    final QueueMessageStatusRepository queueMessageStatusRepository;
    final MessageProducer rabbitMqMessageProducer;
    final ManifestApprovalStatusRepository statusRepository;
    final ExportManifestRepository exportManifestRepository;

    public CheckApprovedExportManifestStatusImpl(QueueMessageStatusRepository queueMessageStatusRepository, MessageProducer rabbitMqMessageProducer, ManifestApprovalStatusRepository statusRepository, ExportManifestRepository exportManifestRepository) {
        this.queueMessageStatusRepository = queueMessageStatusRepository;
        this.rabbitMqMessageProducer = rabbitMqMessageProducer;
        this.statusRepository = statusRepository;
        this.exportManifestRepository = exportManifestRepository;
    }

    @Transactional
    @Scheduled(fixedRate = 1800000)
    public void CheckApprovedExportManifest() {
        List<ManifestApprovalStatus> exportManifest = statusRepository.findByExportApprovedStatusFalse();
        for (ManifestApprovalStatus mf : exportManifest) {
            if (mf.isExportApprovedStatus()) {
                Optional<ExportManifest> optional = exportManifestRepository.findFirstByMrnOut(mf.getMrnOut());
                if (optional.isPresent()) {
                    ExportManifest export = optional.get();
                    if (ManifestStatus.APPROVED.equalsIgnoreCase(export.getProcessingStatus())) {
                        SubmittedExportManifestApprovalNotice submittedNotice = new SubmittedExportManifestApprovalNotice();
                        submittedNotice.setManifestReferenceNumber(export.getMrnOut());
                        submittedNotice.setCommunicationAgreedId(export.getCommunicationAgreedId());
                        submittedNotice.setApprovalStatus(getExportStatus(export.getProcessingStatus()));
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                        submittedNotice.setApprovalDate(formatter.format(export.getProcessingDate()));
                        mf.setExportApprovedStatus(true);
                        LocalDateTime localDateTime = LocalDateTime.now();
                        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                        mf.setExportApprovedDate(localDateTime.format(format));
                        statusRepository.save(mf);
                        String response = sendExportManifestApprovalNoticeToQueue(submittedNotice);
                        LOGGER.info("[Export Manifest Approval Notice]" + response);
                    }
                }

            }
        }

    }

    private String sendExportManifestApprovalNoticeToQueue(SubmittedExportManifestApprovalNotice submittedNotice) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String payload = mapper.writeValueAsString(submittedNotice);
            LOGGER.info("[Export Manifest Notice]" + payload);
            MessageDto messageDto = new MessageDto();
            ExportManifestApprovalNoticeMessageDto exportManifestApprovalNoticeMessageDto = new ExportManifestApprovalNoticeMessageDto();
            exportManifestApprovalNoticeMessageDto.setMessageName(MessageNames.EXPORT_MANIFEST_APPROVAL_NOTICE);
            RequestIdDto requestIdDto = mapper.readValue(getId(), RequestIdDto.class);
            exportManifestApprovalNoticeMessageDto.setRequestId(requestIdDto.getMessageId());
            messageDto.setPayload(submittedNotice);
            AcknowledgementDto queueResponse = rabbitMqMessageProducer.
                    sendMessage(OUTBOUND_EXCHANGE, MessageNames.EXPORT_MANIFEST_APPROVAL_NOTICE, exportManifestApprovalNoticeMessageDto.getRequestId(), messageDto.getCallbackUrl(), messageDto.getPayload());
            LOGGER.info("[payload Submitted To RabbitMQ]" + queueResponse);

            QueueMessageStatusEntity queueMessage = new QueueMessageStatusEntity();
            queueMessage.setMessageId(submittedNotice.getCommunicationAgreedId());
            queueMessage.setReferenceId(exportManifestApprovalNoticeMessageDto.getRequestId());
            queueMessage.setMessageName(MessageNames.EXPORT_MANIFEST_APPROVAL_NOTICE);
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

    private String getExportStatus(String processingStatus) {
        if (processingStatus.equalsIgnoreCase("E")) {
            return "A";
        } else if (processingStatus.equalsIgnoreCase("Z")) {
            return "R";
        } else {
            return processingStatus;
        }
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