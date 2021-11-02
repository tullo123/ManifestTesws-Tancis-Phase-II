package com.ManifestTeswTancis.ServiceImpl;

import com.ManifestTeswTancis.Entity.CustomClearanceApprovalStatus;
import com.ManifestTeswTancis.Entity.CustomClearanceEntity;
import com.ManifestTeswTancis.Entity.QueueMessageStatusEntity;
import com.ManifestTeswTancis.RabbitConfigurations.*;
import com.ManifestTeswTancis.Repository.CustomClearanceApprovalRepository;
import com.ManifestTeswTancis.Repository.CustomClearanceRepository;
import com.ManifestTeswTancis.Repository.QueueMessageStatusRepository;
import com.ManifestTeswTancis.Response.CustomClearanceApprovalResponse;
import com.ManifestTeswTancis.Util.ClearanceStatus;
import com.ManifestTeswTancis.Util.DateFormatter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@Service
public class CheckApprovedCustomClearanceStatusImpl {
    @Value("${spring.rabbitmq.exchange.out}")
    private String OUTBOUND_EXCHANGE;
    final MessageProducer rabbitMqMessageProducer;
    final CustomClearanceRepository customClearanceRepository;
    final CustomClearanceApprovalRepository customClearanceApprovalRepository;
    final QueueMessageStatusRepository queueMessageStatusRepository;

    @Autowired
    public CheckApprovedCustomClearanceStatusImpl(CustomClearanceRepository customClearanceRepository, CustomClearanceApprovalRepository customClearanceApprovalRepository, MessageProducer rabbitMqMessageProducer, QueueMessageStatusRepository queueMessageStatusRepository) {
        this.customClearanceRepository = customClearanceRepository;
        this.customClearanceApprovalRepository = customClearanceApprovalRepository;
        this.rabbitMqMessageProducer = rabbitMqMessageProducer;
        this.queueMessageStatusRepository = queueMessageStatusRepository;
    }

    @Transactional
    @Scheduled(fixedRate = 240000)
    public void CheckCustomClearanceApprovalStatusServiceImpl() {
        List<CustomClearanceApprovalStatus> status = customClearanceApprovalRepository.findByApprovedStatusFalse();
        System.out.println("--------------- Checking for approved  Custom Clearance ---------------");
        for (CustomClearanceApprovalStatus ca : status) {
            if (!ca.isApprovedStatus()) {
                System.out.println("---------- Approving custom clearance with CallId" + ca.getCommunicationAgreedId() + "----------");
                CustomClearanceEntity cs = customClearanceRepository.findFirstByCommunicationAgreedId(ca.getCommunicationAgreedId());
                if (ClearanceStatus.APPROVED.equals(cs.getProcessingStatus())) {
                    CustomClearanceApprovalResponse customClearanceApprovalResponse = new CustomClearanceApprovalResponse();
                    customClearanceApprovalResponse.setCommunicationAgreedId(cs.getCommunicationAgreedId());
                    customClearanceApprovalResponse.setClearanceReference(cs.getTaxClearanceNumber());
                    customClearanceApprovalResponse.setApprovalStatus(getStatus(cs.getProcessingStatus()));
                    customClearanceApprovalResponse.setComment(cs.getComments());
                    customClearanceApprovalResponse.setNoticeDate(DateFormatter.getTeSWSLocalDate(LocalDateTime.now()));
                    ca.setApprovedStatus(true);
                    customClearanceApprovalRepository.save(ca);
                    String response = sendApprovalNoticeToQueue(customClearanceApprovalResponse);
                    System.out.println("--------------- Approval Notice Response --------------\n" + response);
                }
            }
        }

    }
    private String sendApprovalNoticeToQueue(CustomClearanceApprovalResponse customClearanceApprovalResponse) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String payload = mapper.writeValueAsString(customClearanceApprovalResponse);
            System.out.println("--------------- Custom Clearance Approval Notice Payload ---------------\n" + payload);
            MessageDto messageDto = new MessageDto();
            ResponseClearanceMessageDto responseClearanceMessageDto = new ResponseClearanceMessageDto();
            responseClearanceMessageDto.setMessageName(MessageNames.CUSTOM_CLEARANCE_NOTICE);
            RequestIdDto requestIdDto = mapper.readValue(getId(), RequestIdDto.class);
            responseClearanceMessageDto.setRequestId(requestIdDto.getMessageId());
            messageDto.setPayload(customClearanceApprovalResponse);
            AcknowledgementDto queueResponse = rabbitMqMessageProducer.
                    sendMessage(OUTBOUND_EXCHANGE, MessageNames.CUSTOM_CLEARANCE_NOTICE, responseClearanceMessageDto.getRequestId(), messageDto.getCallbackUrl(), messageDto.getPayload());
            System.out.println(queueResponse);
            QueueMessageStatusEntity queueMessage = new QueueMessageStatusEntity();
            queueMessage.setMessageId(customClearanceApprovalResponse.getCommunicationAgreedId());
            queueMessage.setReferenceId(responseClearanceMessageDto.getRequestId());
            queueMessage.setMessageName(MessageNames.CUSTOM_CLEARANCE_NOTICE);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }

    private String getId() throws IOException {
        String url = "http://192.168.30.200:7074/GetId";
        HttpGet request = new HttpGet(url);
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = client.execute(request);
        HttpEntity entity = response.getEntity();

        return EntityUtils.toString(entity);
    }
    private String getStatus(String processingStatus) {
        if (processingStatus.contentEquals("D")) {
            return "A";
        } else if (processingStatus.contentEquals("R")) {
            return "REJECTED";
        } else if (processingStatus.contentEquals("B")) {
            return "RECEIVED";
        } else return processingStatus;
    }
}

