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
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class CheckApprovedCustomClearanceStatusImpl {
    private static final Logger LOGGER = LoggerFactory.getLogger(CheckApprovedCustomClearanceStatusImpl.class);
    @Value("${spring.rabbitmq.exchange.out}")
    private String OUTBOUND_EXCHANGE;
    @Value("${url}")
    private String url;
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
        LOGGER.info("--- Checking for approved  Custom Clearance ---");
        for (CustomClearanceApprovalStatus ca : status) {
            if (!ca.isApprovedStatus()) {
                LOGGER.info("--- Approving custom clearance with CallId" + ca.getCommunicationAgreedId() + "----");
                CustomClearanceEntity cs = customClearanceRepository.findFirstByCommunicationAgreedId(ca.getCommunicationAgreedId());
                if (ClearanceStatus.APPROVED.equals(cs.getProcessingStatus())) {
                    CustomClearanceApprovalResponse customClearanceApprovalResponse = new CustomClearanceApprovalResponse();
                    customClearanceApprovalResponse.setCommunicationAgreedId(cs.getCommunicationAgreedId());
                    customClearanceApprovalResponse.setClearanceReference(cs.getTaxClearanceNumber());
                    customClearanceApprovalResponse.setApprovalStatus(getStatus(cs.getProcessingStatus()));
                    customClearanceApprovalResponse.setComment(cs.getComments());
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    customClearanceApprovalResponse.setNoticeDate(formatter.format(cs.getAuditDate()));
                    ca.setRejectedYn("N");
                    ca.setApprovedStatus(true);
                    customClearanceApprovalRepository.save(ca);
                    String response = sendApprovalNoticeToQueue(customClearanceApprovalResponse);
                    LOGGER.info("---Approval Notice---\n" + response);
                }

                if(ClearanceStatus.REJECTED.equals(cs.getProcessingStatus())){
                    CustomClearanceApprovalResponse customClearanceApprovalResponse = new CustomClearanceApprovalResponse();
                    customClearanceApprovalResponse.setCommunicationAgreedId(cs.getCommunicationAgreedId());
                    customClearanceApprovalResponse.setClearanceReference(cs.getTaxClearanceNumber());
                    customClearanceApprovalResponse.setApprovalStatus(getStatus(cs.getProcessingStatus()));
                    customClearanceApprovalResponse.setComment(cs.getComments());
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    customClearanceApprovalResponse.setNoticeDate(formatter.format(cs.getAuditDate()));
                    ca.setRejectedYn("Y");
                    ca.setApprovedStatus(true);
                    customClearanceApprovalRepository.save(ca);
                    String response = sendApprovalNoticeToQueue(customClearanceApprovalResponse);
                    LOGGER.info("---Rejection Notice---\n" + response);
                }
            }
        }

    }
    private String sendApprovalNoticeToQueue(CustomClearanceApprovalResponse customClearanceApprovalResponse) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String payload = mapper.writeValueAsString(customClearanceApprovalResponse);
            LOGGER.info("---Custom Clearance Approval Notice ---\n" + payload);
            MessageDto messageDto = new MessageDto();
            ResponseClearanceMessageDto responseClearanceMessageDto = new ResponseClearanceMessageDto();
            responseClearanceMessageDto.setMessageName(MessageNames.CUSTOM_CLEARANCE_NOTICE);
            RequestIdDto requestIdDto = mapper.readValue(getId(), RequestIdDto.class);
            responseClearanceMessageDto.setRequestId(requestIdDto.getMessageId());
            messageDto.setPayload(customClearanceApprovalResponse);
            AcknowledgementDto queueResponse = rabbitMqMessageProducer.
                    sendMessage(OUTBOUND_EXCHANGE, MessageNames.CUSTOM_CLEARANCE_NOTICE, responseClearanceMessageDto.getRequestId(), messageDto.getCallbackUrl(), messageDto.getPayload());
            LOGGER.info("[payload Submitted To RabbitMQ]" +queueResponse);
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
        } else return processingStatus;
    }
}

