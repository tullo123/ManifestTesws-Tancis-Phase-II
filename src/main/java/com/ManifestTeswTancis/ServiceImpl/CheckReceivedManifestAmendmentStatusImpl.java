package com.ManifestTeswTancis.ServiceImpl;

import com.ManifestTeswTancis.Entity.ExImportAmendGeneral;
import com.ManifestTeswTancis.Entity.ManifestAmendmentApprovalStatus;
import com.ManifestTeswTancis.Entity.QueueMessageStatusEntity;
import com.ManifestTeswTancis.RabbitConfigurations.*;
import com.ManifestTeswTancis.Repository.ExImportAmendGeneralRepository;
import com.ManifestTeswTancis.Repository.ManifestAmendmentApprovalStatusRepository;
import com.ManifestTeswTancis.Repository.QueueMessageStatusRepository;
import com.ManifestTeswTancis.Response.ManifestAmendmentReceivedRejectedStatus;
import com.ManifestTeswTancis.Util.DateFormatter;
import com.ManifestTeswTancis.Util.ManifestAmendmentStatus;
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
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@Service
public class CheckReceivedManifestAmendmentStatusImpl {
    @Value("${spring.rabbitmq.exchange.out}")
    private String OUTBOUND_EXCHANGE;
    final MessageProducer rabbitMqMessageProducer;
    final QueueMessageStatusRepository queueMessageStatusRepository;
    final ManifestAmendmentApprovalStatusRepository manifestAmendmentApprovalStatusRepository;
    final ExImportAmendGeneralRepository exImportAmendGeneralRepository;

    public CheckReceivedManifestAmendmentStatusImpl(MessageProducer rabbitMqMessageProducer, QueueMessageStatusRepository queueMessageStatusRepository, ManifestAmendmentApprovalStatusRepository manifestAmendmentApprovalStatusRepository, ExImportAmendGeneralRepository exImportAmendGeneralRepository) {
        this.rabbitMqMessageProducer = rabbitMqMessageProducer;
        this.queueMessageStatusRepository = queueMessageStatusRepository;
        this.manifestAmendmentApprovalStatusRepository = manifestAmendmentApprovalStatusRepository;
        this.exImportAmendGeneralRepository = exImportAmendGeneralRepository;
    }
    @Transactional
    @Scheduled(fixedRate = 300000)
    public void CheckManifestAmendmentReceivedStatusImpl(){
        List<ManifestAmendmentApprovalStatus> status=manifestAmendmentApprovalStatusRepository.findByReceivedNoticeSentFalse();
        for(ManifestAmendmentApprovalStatus ma: status ){
            if(!ma.isApprovedStatus()){
                ExImportAmendGeneral general=exImportAmendGeneralRepository.findByMrn(ma.getMrn());
                if(ManifestAmendmentStatus.RECEIVED.equals(general.getProcessingStatus()) || ManifestAmendmentStatus.REJECTED.equals(general.getProcessingStatus())){
                    ManifestAmendmentReceivedRejectedStatus manifestAmendmentReceivedRejectedStatus = new ManifestAmendmentReceivedRejectedStatus();
                    manifestAmendmentReceivedRejectedStatus.setCommunicationAgreedId(ma.getCommunicationAgreedId());
                    manifestAmendmentReceivedRejectedStatus.setNoticeDate(DateFormatter.getTeSWSLocalDate(LocalDateTime.now()));
                    manifestAmendmentReceivedRejectedStatus.setAmendmentReference(ma.getAmendReference());
                    manifestAmendmentReceivedRejectedStatus.setMrn(ma.getMrn());
                    manifestAmendmentReceivedRejectedStatus.setVoyageNumber(ma.getVoyageNumber());
                    manifestAmendmentReceivedRejectedStatus.setStatus(getStatus(general.getProcessingStatus()));
                    manifestAmendmentReceivedRejectedStatus.setMsn(general.getMsn());
                    manifestAmendmentReceivedRejectedStatus.setHsn(general.getHsn());
                    if(general.getHsn()!=null){
                        manifestAmendmentReceivedRejectedStatus.setCrn(general.getMrn()+general.getMsn()+general.getHsn());
                    }else{
                        manifestAmendmentReceivedRejectedStatus.setCrn(general.getMrn()+general.getMsn());
                    }
                    manifestAmendmentReceivedRejectedStatus.setComment(general.getAuditComment());
                    ma.setReceivedNoticeSent(true);
                    ma.setReceivedNoticeDate(DateFormatter.getTeSWSLocalDate(LocalDateTime.now()));
                    manifestAmendmentApprovalStatusRepository.save(ma);
                    String response = sendReceivedManifestAmendmentNoticeToQueue(manifestAmendmentReceivedRejectedStatus);
                    System.out.println("--------------- Received/Failed Notice Response --------------\n" + response);
                }
            }
        }
    }

    private String sendReceivedManifestAmendmentNoticeToQueue(ManifestAmendmentReceivedRejectedStatus manifestAmendmentReceivedRejectedStatus) {
        ObjectMapper mapper = new ObjectMapper();
        try{
            String payload = mapper.writeValueAsString(manifestAmendmentReceivedRejectedStatus);
            System.out.println("----------- Received/Failed Notice Response ------------\n"+payload);
            MessageDto messageDto = new MessageDto();
            ManifestAmendmentStatusMessageDto manifestAmendmentStatusMessageDto = new ManifestAmendmentStatusMessageDto();
            manifestAmendmentStatusMessageDto.setMessageName(MessageNames.MANIFEST_AMENDMENT_STATUS);
            RequestIdDto requestIdDto = mapper.readValue(getId(), RequestIdDto.class);
            manifestAmendmentStatusMessageDto.setRequestId(requestIdDto.getMessageId());
            messageDto.setPayload(manifestAmendmentReceivedRejectedStatus);
            AcknowledgementDto queueResponse = rabbitMqMessageProducer.
                    sendMessage(OUTBOUND_EXCHANGE, MessageNames.MANIFEST_AMENDMENT_STATUS, manifestAmendmentStatusMessageDto.getRequestId(), messageDto.getCallbackUrl(), messageDto.getPayload());
            System.out.println(queueResponse);
            QueueMessageStatusEntity queueMessage = new QueueMessageStatusEntity();
            queueMessage.setMessageId(manifestAmendmentReceivedRejectedStatus.getCommunicationAgreedId());
            queueMessage.setReferenceId(manifestAmendmentStatusMessageDto.getRequestId());
            queueMessage.setMessageName(MessageNames.MANIFEST_AMENDMENT_STATUS);
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

    private String getId() throws IOException {
        String url = "http://192.168.30.200:7074/GetId";
        HttpGet request = new HttpGet(url);
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = client.execute(request);
        HttpEntity entity = response.getEntity();

        return EntityUtils.toString(entity);
    }


    public String getStatus(String processingStatus) {
        if(processingStatus.contentEquals("B")){
            return "RECEIVED";
        }else if (processingStatus.contentEquals("R")){
            return "REJECTED";
        }
        else return processingStatus;
    }
}
