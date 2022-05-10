package com.ManifestTeswTancis.ServiceImpl;

import com.ManifestTeswTancis.Entity.ExImportManifest;
import com.ManifestTeswTancis.Entity.ManifestApprovalStatus;
import com.ManifestTeswTancis.Entity.QueueMessageStatusEntity;
import com.ManifestTeswTancis.RabbitConfigurations.*;
import com.ManifestTeswTancis.Repository.ExImportManifestRepository;
import com.ManifestTeswTancis.Repository.ManifestApprovalStatusRepository;
import com.ManifestTeswTancis.Repository.QueueMessageStatusRepository;
import com.ManifestTeswTancis.Response.SubmittedManifestStatusResponse;
import com.ManifestTeswTancis.Util.DateFormatter;
import com.ManifestTeswTancis.Util.ManifestStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
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

@Component
@Service
public class CheckSubmittedManifestStatusImpl {
    @Value("${spring.rabbitmq.exchange.out}")
    private String OUTBOUND_EXCHANGE;
    @Value("${url}")
    private String url;
    final QueueMessageStatusRepository queueMessageStatusRepository;
    final MessageProducer rabbitMqMessageProducer;
    final ManifestApprovalStatusRepository statusRepository;
    final ExImportManifestRepository exImportManifestRepository;

    public CheckSubmittedManifestStatusImpl(ManifestApprovalStatusRepository statusRepository, ExImportManifestRepository exImportManifestRepository, MessageProducer rabbitMqMessageProducer, QueueMessageStatusRepository queueMessageStatusRepository) {
        this.statusRepository = statusRepository;
        this.exImportManifestRepository = exImportManifestRepository;
        this.rabbitMqMessageProducer = rabbitMqMessageProducer;
        this.queueMessageStatusRepository = queueMessageStatusRepository;
    }

    @Transactional
    @Scheduled(fixedRate = 120000)
    public void checkReceivedManifestStatus() {
        List<ManifestApprovalStatus> manifestStatusEntities = statusRepository.findByReceivedNoticeSentFalse();
        for (ManifestApprovalStatus mf : manifestStatusEntities) {
            if (!mf.isApprovedStatus()) {
                ExImportManifest callInf = exImportManifestRepository.findByMrn(mf.getMrn());
                if (ManifestStatus.RECEIVED.equals(callInf.getProcessingStatus())) {
                    SubmittedManifestStatusResponse submittedManifestStatusResponse = new SubmittedManifestStatusResponse();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    submittedManifestStatusResponse.setNoticeDate(formatter.format(callInf.getProcessingDate()));
                    submittedManifestStatusResponse.setCommunicationAgreedId(callInf.getCommunicationAgreedId());
                    submittedManifestStatusResponse.setControlReferenceNumber(callInf.getControlReferenceNumber());
                    submittedManifestStatusResponse.setApplicationReference(callInf.getControlReferenceNumber());
                    submittedManifestStatusResponse.setVoyageNumber(callInf.getVoyageNumber());
                    submittedManifestStatusResponse.setCustomOfficeCode(callInf.getCustomOfficeCode());
                    submittedManifestStatusResponse.setStatus(getStatus(callInf.getProcessingStatus()));
                    mf.setProcessingStatus(getStatus(callInf.getProcessingStatus()));
                    mf.setReceivedNoticeSent(true);
                    LocalDateTime localDateTime = LocalDateTime.now();
                    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                    mf.setNoticeDate(localDateTime.format(format));
                    statusRepository.save(mf);
                    String response = sendSubmittedManifestStatusToQueue(submittedManifestStatusResponse);
                    System.out.println("--- Approval Notice---\n" + response);
                }

                if(ManifestStatus.CANCELED.equals(callInf.getProcessingStatus())){
                    mf.setCancellationDate(DateFormatter.getTeSWSLocalDate(LocalDateTime.now()));
                    mf.setCancellationStatus("CANCELED");
                    mf.setApprovedStatus(true);
                    mf.setReceivedNoticeSent(true);
                    statusRepository.save(mf);
                }
            }
        }
    }

    private String sendSubmittedManifestStatusToQueue(SubmittedManifestStatusResponse submittedManifestStatusResponse) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String payload = mapper.writeValueAsString(submittedManifestStatusResponse);
            System.out.println("--- Submitted Manifest Status ---\n"+payload);
            MessageDto messageDto = new MessageDto();
            SubmittedManifestStatusMessageDto submittedManifestStatusMessageDto = new SubmittedManifestStatusMessageDto();
            submittedManifestStatusMessageDto.setMessageName(MessageNames.SUBMITTED_MANIFEST_STATUS);
            RequestIdDto requestIdDto = mapper.readValue(getId(), RequestIdDto.class);
            submittedManifestStatusMessageDto.setRequestId(requestIdDto.getMessageId());
            messageDto.setPayload(submittedManifestStatusResponse);
            AcknowledgementDto queueResponse = rabbitMqMessageProducer.
                    sendMessage(OUTBOUND_EXCHANGE, MessageNames.SUBMITTED_MANIFEST_STATUS, submittedManifestStatusMessageDto.getRequestId(), messageDto.getCallbackUrl(), messageDto.getPayload());
            System.out.println(queueResponse);
            QueueMessageStatusEntity queueMessage = new QueueMessageStatusEntity();
            queueMessage.setMessageId(submittedManifestStatusResponse.getCommunicationAgreedId());
            queueMessage.setReferenceId(submittedManifestStatusMessageDto.getRequestId());
            queueMessage.setMessageName(MessageNames.SUBMITTED_MANIFEST_STATUS);
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "success";
    }


    private String getStatus(String processingStatus) {
        if (processingStatus.contentEquals("B")) {
            return "RECEIVED";
        }
        else {
            return processingStatus;
        }
    }
    private String getId() throws IOException {
        HttpGet request = new HttpGet(url);
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = client.execute(request);
        HttpEntity entity = response.getEntity();
        return EntityUtils.toString(entity);
    }
}