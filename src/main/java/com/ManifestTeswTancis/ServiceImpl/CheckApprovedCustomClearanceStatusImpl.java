package com.ManifestTeswTancis.ServiceImpl;

import com.ManifestTeswTancis.Entity.CustomClearanceApprovalStatus;
import com.ManifestTeswTancis.Entity.CustomClearanceEntity;
import com.ManifestTeswTancis.RabbitConfigurations.*;
import com.ManifestTeswTancis.Repository.CustomClearanceApprovalRepository;
import com.ManifestTeswTancis.Repository.CustomClearanceRepository;
import com.ManifestTeswTancis.Response.ResponseCustomClearance;
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
import java.util.List;

@Component
@Service
public class CheckApprovedCustomClearanceStatusImpl {
    @Value("${spring.rabbitmq.exchange.out}")
    private String OUTBOUND_EXCHANGE;
    final MessageProducer rabbitMqMessageProducer;
    final CustomClearanceRepository customClearanceRepository;
    final CustomClearanceApprovalRepository customClearanceApprovalRepository;

    @Autowired
    public CheckApprovedCustomClearanceStatusImpl(CustomClearanceRepository customClearanceRepository, CustomClearanceApprovalRepository customClearanceApprovalRepository, MessageProducer rabbitMqMessageProducer) {
        this.customClearanceRepository = customClearanceRepository;
        this.customClearanceApprovalRepository = customClearanceApprovalRepository;
        this.rabbitMqMessageProducer = rabbitMqMessageProducer;
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
                    ResponseCustomClearance responseCustomClearance = new ResponseCustomClearance();
                    responseCustomClearance.setCommunicationAgreedId(cs.getCommunicationAgreedId());
                    responseCustomClearance.setClearanceReference(cs.getTaxClearanceNumber());
                    responseCustomClearance.setApprovalStatus(getStatus(cs.getProcessingStatus()));
                    responseCustomClearance.setComment(cs.getComments());
                    responseCustomClearance.setNoticeDate(DateFormatter.getTeSWSLocalDate(LocalDateTime.now()));
                    ca.setApprovedStatus(true);
                    customClearanceApprovalRepository.save(ca);
                    String response = sendApprovalNoticeToQueue(responseCustomClearance);
                    System.out.println("--------------- Approval Notice Response --------------\n" + response);
                }
            }
        }

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


    private String sendApprovalNoticeToQueue(ResponseCustomClearance responseCustomClearance) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String payload = mapper.writeValueAsString(responseCustomClearance);
            System.out.println("--------------- Custom Clearance Approval Notice Payload ---------------\n" + payload);
            MessageDto messageDto = new MessageDto();
            ResponseClearanceMessageDto responseClearanceMessageDto = new ResponseClearanceMessageDto();
            responseClearanceMessageDto.setMessageName(MessageNames.CUSTOM_CLEARANCE_NOTICE);
            RequestIdDto requestIdDto = mapper.readValue(getId(), RequestIdDto.class);
            responseClearanceMessageDto.setRequestId(requestIdDto.getMessageId());
            messageDto.setPayload(responseCustomClearance);
            AcknowledgementDto queueResponse = rabbitMqMessageProducer.
                    sendMessage(OUTBOUND_EXCHANGE, MessageNames.CUSTOM_CLEARANCE_NOTICE, responseClearanceMessageDto.getRequestId(), messageDto.getCallbackUrl(), messageDto.getPayload());
            System.out.println(queueResponse);
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
}

