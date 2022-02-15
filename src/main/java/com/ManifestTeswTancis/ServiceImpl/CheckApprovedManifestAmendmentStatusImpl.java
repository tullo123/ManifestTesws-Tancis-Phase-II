package com.ManifestTeswTancis.ServiceImpl;

import com.ManifestTeswTancis.Entity.ExImportAmendGeneral;
import com.ManifestTeswTancis.Entity.ManifestAmendmentApprovalStatus;
import com.ManifestTeswTancis.Entity.QueueMessageStatusEntity;
import com.ManifestTeswTancis.RabbitConfigurations.*;
import com.ManifestTeswTancis.Repository.*;
import com.ManifestTeswTancis.Response.ManifestAmendmentApprovalStatusResponse;
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
import java.util.Optional;

@Component
@Service
public class CheckApprovedManifestAmendmentStatusImpl {
    @Value("${spring.rabbitmq.exchange.out}")
    private String OUTBOUND_EXCHANGE;
    final MessageProducer rabbitMqMessageProducer;
    final QueueMessageStatusRepository queueMessageStatusRepository;
    final ManifestAmendmentApprovalStatusRepository manifestAmendmentApprovalStatusRepository;
    final ExImportAmendGeneralRepository exImportAmendGeneralRepository;
    final InImportAmendGeneralRepository inImportAmendGeneralRepository;

    public CheckApprovedManifestAmendmentStatusImpl(MessageProducer rabbitMqMessageProducer, QueueMessageStatusRepository queueMessageStatusRepository, ManifestAmendmentApprovalStatusRepository manifestAmendmentApprovalStatusRepository, ExImportAmendGeneralRepository exImportAmendGeneralRepository, InImportAmendGeneralRepository inImportAmendGeneralRepository) {
        this.rabbitMqMessageProducer = rabbitMqMessageProducer;
        this.queueMessageStatusRepository = queueMessageStatusRepository;
        this.manifestAmendmentApprovalStatusRepository = manifestAmendmentApprovalStatusRepository;
        this.exImportAmendGeneralRepository = exImportAmendGeneralRepository;
        this.inImportAmendGeneralRepository = inImportAmendGeneralRepository;
    }
    @Transactional
    @Scheduled(fixedRate = 700000)
    public void CheckManifestAmendmentApprovalStatusImpl(){
        List<ManifestAmendmentApprovalStatus> status=manifestAmendmentApprovalStatusRepository.findByApprovedStatusFalse();
        for(ManifestAmendmentApprovalStatus ma: status ){
            if(!ma.isApprovedStatus()) {
                Optional<ExImportAmendGeneral> optional = exImportAmendGeneralRepository.findFirstByMrnAndAmendSerialNumber(ma.getMrn(), ma.getAmendSerialNo());
                if (optional.isPresent()) {
                    ExImportAmendGeneral general = optional.get();
                    if (ManifestAmendmentStatus.APPROVED.equals(general.getProcessingStatus())) {
                        ManifestAmendmentApprovalStatusResponse manifestAmendmentApprovalStatusResponse = new ManifestAmendmentApprovalStatusResponse();
                        manifestAmendmentApprovalStatusResponse.setCommunicationAgreedId(ma.getCommunicationAgreedId());
                        manifestAmendmentApprovalStatusResponse.setNoticeDate(DateFormatter.getTeSWSLocalDate(LocalDateTime.now()));
                        manifestAmendmentApprovalStatusResponse.setAmendmentReference(ma.getAmendReference());
                        manifestAmendmentApprovalStatusResponse.setMrn(general.getMrn());
                        manifestAmendmentApprovalStatusResponse.setVoyageNumber(ma.getVoyageNumber());
                        manifestAmendmentApprovalStatusResponse.setApprovalStatus(getStatus(general.getProcessingStatus()));
                        manifestAmendmentApprovalStatusResponse.setMsn(general.getMsn());
                        manifestAmendmentApprovalStatusResponse.setHsn(general.getHsn());
                        if (general.getHsn() != null) {
                            manifestAmendmentApprovalStatusResponse.setCrn(general.getMrn() + general.getMsn() + general.getHsn());
                        } else {
                            manifestAmendmentApprovalStatusResponse.setCrn(general.getMrn() + general.getMsn());
                        }
                        manifestAmendmentApprovalStatusResponse.setComment(general.getAuditComment());
                        ma.setRejectedYn("N");
                        ma.setApprovedStatus(true);
                        manifestAmendmentApprovalStatusRepository.save(ma);
                        String response = sendApprovalNoticeToQueue(manifestAmendmentApprovalStatusResponse);
                        System.out.println("---- Approval Notice  -----\n" + response);

                    }

                    if(ManifestAmendmentStatus.REJECTED.equals(general.getProcessingStatus())){
                        ManifestAmendmentApprovalStatusResponse manifestAmendmentApprovalStatusResponse = new ManifestAmendmentApprovalStatusResponse();
                        manifestAmendmentApprovalStatusResponse.setCommunicationAgreedId(ma.getCommunicationAgreedId());
                        manifestAmendmentApprovalStatusResponse.setNoticeDate(DateFormatter.getTeSWSLocalDate(LocalDateTime.now()));
                        manifestAmendmentApprovalStatusResponse.setAmendmentReference(ma.getAmendReference());
                        manifestAmendmentApprovalStatusResponse.setMrn(general.getMrn());
                        manifestAmendmentApprovalStatusResponse.setVoyageNumber(ma.getVoyageNumber());
                        manifestAmendmentApprovalStatusResponse.setApprovalStatus(getStatus(general.getProcessingStatus()));
                        manifestAmendmentApprovalStatusResponse.setMsn(general.getMsn());
                        manifestAmendmentApprovalStatusResponse.setHsn(general.getHsn());
                        if (general.getHsn() != null) {
                            manifestAmendmentApprovalStatusResponse.setCrn(general.getMrn() + general.getMsn() + general.getHsn());
                        } else {
                            manifestAmendmentApprovalStatusResponse.setCrn(general.getMrn() + general.getMsn());
                        }
                        manifestAmendmentApprovalStatusResponse.setComment(general.getAuditComment());
                        ma.setRejectedYn("Y");
                        ma.setApprovedStatus(true);
                        manifestAmendmentApprovalStatusRepository.save(ma);
                        String response = sendApprovalNoticeToQueue(manifestAmendmentApprovalStatusResponse);
                        System.out.println("----- Approval Notice ------\n" + response);

                    }
                }
            }
        }
    }

    private String sendApprovalNoticeToQueue(ManifestAmendmentApprovalStatusResponse manifestAmendmentApprovalStatusResponse) {
        ObjectMapper mapper = new ObjectMapper();
        try{
            String payload = mapper.writeValueAsString(manifestAmendmentApprovalStatusResponse);
            System.out.println("------- Approval notice payload -------\n"+payload);
            MessageDto messageDto = new MessageDto();
            ManifestAmendmentNoticeMessageDto manifestAmendmentNoticeMessageDto = new ManifestAmendmentNoticeMessageDto();
            manifestAmendmentNoticeMessageDto.setMessageName(MessageNames.MANIFEST_AMENDMENT_NOTICE);
            RequestIdDto requestIdDto = mapper.readValue( getId(), RequestIdDto.class);
            manifestAmendmentNoticeMessageDto.setRequestId(requestIdDto.getMessageId());
            messageDto.setPayload(manifestAmendmentApprovalStatusResponse);
            AcknowledgementDto queueResponse = rabbitMqMessageProducer.
                    sendMessage(OUTBOUND_EXCHANGE, MessageNames.MANIFEST_AMENDMENT_NOTICE, manifestAmendmentNoticeMessageDto.getRequestId(), messageDto.getCallbackUrl(), messageDto.getPayload());
            System.out.println(queueResponse);
            QueueMessageStatusEntity queueMessage = new QueueMessageStatusEntity();
            queueMessage.setMessageId(manifestAmendmentApprovalStatusResponse.getCommunicationAgreedId());
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
        }else if(processingStatus.contentEquals("E")){
            return "R";
        }
        else return processingStatus;
    }
}
