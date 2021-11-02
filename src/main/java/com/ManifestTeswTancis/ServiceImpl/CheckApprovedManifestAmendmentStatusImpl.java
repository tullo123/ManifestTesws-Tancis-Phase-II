package com.ManifestTeswTancis.ServiceImpl;

import com.ManifestTeswTancis.Entity.ExImportAmendGeneral;
import com.ManifestTeswTancis.Entity.ManifestAmendmentApprovalStatus;
import com.ManifestTeswTancis.Entity.QueueMessageStatusEntity;
import com.ManifestTeswTancis.RabbitConfigurations.*;
import com.ManifestTeswTancis.Repository.*;
import com.ManifestTeswTancis.Response.ManifestAmendmentApprovalResponseStatus;
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
public class CheckApprovedManifestAmendmentStatusImpl {
    @Value("${spring.rabbitmq.exchange.out}")
    private String OUTBOUND_EXCHANGE;
    final MessageProducer rabbitMqMessageProducer;
    final QueueMessageStatusRepository queueMessageStatusRepository;
    final ManifestAmendmentApprovalStatusRepository manifestAmendmentApprovalStatusRepository;
    final ExImportAmendGeneralRepository exImportAmendGeneralRepository;

    public CheckApprovedManifestAmendmentStatusImpl(MessageProducer rabbitMqMessageProducer, QueueMessageStatusRepository queueMessageStatusRepository, ManifestAmendmentApprovalStatusRepository manifestAmendmentApprovalStatusRepository, ExImportAmendGeneralRepository exImportAmendGeneralRepository) {
        this.rabbitMqMessageProducer = rabbitMqMessageProducer;
        this.queueMessageStatusRepository = queueMessageStatusRepository;
        this.manifestAmendmentApprovalStatusRepository = manifestAmendmentApprovalStatusRepository;
        this.exImportAmendGeneralRepository = exImportAmendGeneralRepository;
    }
    @Transactional
    @Scheduled(fixedRate = 400000)
    public void CheckManifestAmendmentApprovalStatusImpl(){
        List<ManifestAmendmentApprovalStatus> status=manifestAmendmentApprovalStatusRepository.findByApprovedStatusFalse();
        for(ManifestAmendmentApprovalStatus ma: status ){
            if(!ma.isApprovedStatus()){
                ExImportAmendGeneral general=exImportAmendGeneralRepository.findByMrn(ma.getMrn());
                if(ManifestAmendmentStatus.APPROVED.equals(general.getProcessingStatus())){
                    ManifestAmendmentApprovalResponseStatus manifestAmendmentApprovalResponseStatus = new ManifestAmendmentApprovalResponseStatus();
                    manifestAmendmentApprovalResponseStatus.setCommunicationAgreedId(ma.getCommunicationAgreedId());
                    manifestAmendmentApprovalResponseStatus.setNoticeDate(DateFormatter.getTeSWSLocalDate(LocalDateTime.now()));
                    manifestAmendmentApprovalResponseStatus.setAmendmentReference(ma.getAmendReference());
                    manifestAmendmentApprovalResponseStatus.setMrn(general.getMrn());
                    manifestAmendmentApprovalResponseStatus.setVoyageNumber(ma.getVoyageNumber());
                    manifestAmendmentApprovalResponseStatus.setApprovalStatus(getStatus(general.getProcessingStatus()));
                    manifestAmendmentApprovalResponseStatus.setMsn(general.getMsn());
                    manifestAmendmentApprovalResponseStatus.setHsn(general.getHsn());
                    if(general.getHsn()!=null){
                        manifestAmendmentApprovalResponseStatus.setCrn(general.getMrn()+general.getMsn()+general.getHsn());
                    }else{
                        manifestAmendmentApprovalResponseStatus.setCrn(general.getMrn()+general.getMsn());
                    }
                    manifestAmendmentApprovalResponseStatus.setComment(general.getAuditComment());
                    ma.setApprovedStatus(true);
                    manifestAmendmentApprovalStatusRepository.save(ma);
                    String response = sendApprovalNoticeToQueue(manifestAmendmentApprovalResponseStatus);
                    System.out.println("--------------- Approval Notice Response --------------\n" + response);

                }
            }
        }
    }

    private String sendApprovalNoticeToQueue(ManifestAmendmentApprovalResponseStatus manifestAmendmentApprovalResponseStatus) {
        ObjectMapper mapper = new ObjectMapper();
        try{
            String payload = mapper.writeValueAsString(manifestAmendmentApprovalResponseStatus);
            System.out.println("----------- Approval notice payload ------------\n"+payload);
            MessageDto messageDto = new MessageDto();
            ManifestAmendmentNoticeMessageDto manifestAmendmentNoticeMessageDto = new ManifestAmendmentNoticeMessageDto();
            manifestAmendmentNoticeMessageDto.setMessageName(MessageNames.MANIFEST_AMENDMENT_NOTICE);
            RequestIdDto requestIdDto = mapper.readValue( getId(), RequestIdDto.class);
            manifestAmendmentNoticeMessageDto.setRequestId(requestIdDto.getMessageId());
            messageDto.setPayload(manifestAmendmentApprovalResponseStatus);
            AcknowledgementDto queueResponse = rabbitMqMessageProducer.
                    sendMessage(OUTBOUND_EXCHANGE, MessageNames.MANIFEST_AMENDMENT_NOTICE, manifestAmendmentNoticeMessageDto.getRequestId(), messageDto.getCallbackUrl(), messageDto.getPayload());
            System.out.println(queueResponse);
            QueueMessageStatusEntity queueMessage = new QueueMessageStatusEntity();
            queueMessage.setMessageId(manifestAmendmentApprovalResponseStatus.getCommunicationAgreedId());
            queueMessage.setReferenceId(manifestAmendmentNoticeMessageDto.getRequestId());
            queueMessage.setMessageName(MessageNames.MANIFEST_AMENDMENT_NOTICE);
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

    private String getStatus(String processingStatus) {
        if (processingStatus.contentEquals("D")) {
            return "A";
        } else return processingStatus;
    }
}
