package com.ManifestTeswTancis.ServiceImpl;

import com.ManifestTeswTancis.Entity.CustomClearanceApprovalStatus;
import com.ManifestTeswTancis.Entity.CustomClearanceEntity;
import com.ManifestTeswTancis.RabbitConfigurations.*;
import com.ManifestTeswTancis.Repository.CustomClearanceApprovalRepository;
import com.ManifestTeswTancis.Repository.CustomClearanceRepository;
import com.ManifestTeswTancis.Response.CustomClearanceStatus;
import com.ManifestTeswTancis.Util.ClearanceStatus;
import com.ManifestTeswTancis.Util.DateFormatter;
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
import java.time.LocalDateTime;
import java.util.List;

@Component
@Service
public class CheckReceivedCustomClearanceStatusImpl {
        @Value("${spring.rabbitmq.exchange.out}")
        private String OUTBOUND_EXCHANGE;
    final CustomClearanceRepository customClearanceRepository;
    final CustomClearanceApprovalRepository customClearanceApprovalRepository;
    final MessageProducer rabbitMqMessageProducer;

    public CheckReceivedCustomClearanceStatusImpl(CustomClearanceRepository customClearanceRepository, CustomClearanceApprovalRepository customClearanceApprovalRepository, MessageProducer rabbitMqMessageProducer) {
        this.customClearanceRepository = customClearanceRepository;
        this.customClearanceApprovalRepository = customClearanceApprovalRepository;
        this.rabbitMqMessageProducer = rabbitMqMessageProducer;
    }

    @Transactional
    @Scheduled(fixedRate=180000)
    public void CheckCustomClearanceReceiveStatusImpl() {
        List<CustomClearanceApprovalStatus> status = customClearanceApprovalRepository.findByReceivedNoticeSentFalse();
        for (CustomClearanceApprovalStatus ca : status) {
            if (!ca.isApprovedStatus()) {
                CustomClearanceEntity cs = customClearanceRepository.findFirstByCommunicationAgreedId(ca.getCommunicationAgreedId());
                if (ClearanceStatus.RECEIVED.equals(cs.getProcessingStatus())|| ClearanceStatus.REJECTED.equals(cs.getProcessingStatus())) {
                    CustomClearanceStatus customClearanceStatus = new CustomClearanceStatus();
                    customClearanceStatus.setNoticeDate(DateFormatter.getTeSWSLocalDate(LocalDateTime.now()));
                    customClearanceStatus.setCommunicationAgreedId(cs.getCommunicationAgreedId());
                    customClearanceStatus.setStatus(getStatus(cs.getProcessingStatus()));
                    ca.setReceivedFailedStatus(getStatus(cs.getProcessingStatus()));
                    ca.setNoticeDate(customClearanceStatus.getNoticeDate());
                    ca.setReceivedNoticeSent(true);
                    customClearanceApprovalRepository.save(ca);
                    String response = sendStatusNoticeToQueue(customClearanceStatus);
                    System.out.println("--------------- Status Notice Response ---------------\n" + response);
                }
            }

        }
    }

    private String sendStatusNoticeToQueue(CustomClearanceStatus customClearanceStatus) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String payload = mapper.writeValueAsString(customClearanceStatus);
            System.out.println("-------------- Custom Status Payload --------------\n"+payload);
            MessageDto messageDto = new MessageDto();
            CustomClearanceMessageStatusDto customClearanceMessageStatusDto = new CustomClearanceMessageStatusDto();
            customClearanceMessageStatusDto.setMessageName(MessageNames.CUSTOM_CLEARANCE_STATUS);
            RequestIdDto requestIdDto = mapper.readValue(getId(), RequestIdDto.class);
            customClearanceMessageStatusDto.setRequestId(requestIdDto.getMessageId());
            messageDto.setPayload(customClearanceStatus);
            AcknowledgementDto queueResponse =rabbitMqMessageProducer.
                    sendMessage(OUTBOUND_EXCHANGE, MessageNames.CUSTOM_CLEARANCE_STATUS,customClearanceMessageStatusDto.getRequestId(),messageDto.getCallbackUrl(),messageDto.getPayload());
            System.out.println(queueResponse);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }

    public String getStatus(String processingStatus) {
        if(processingStatus.contentEquals("D")){
            return "A";
        }else if (processingStatus.contentEquals("R")){
            return "REJECTED";
        } else if (processingStatus.contentEquals("B")){
            return "RECEIVED";
        }
        else return processingStatus;
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