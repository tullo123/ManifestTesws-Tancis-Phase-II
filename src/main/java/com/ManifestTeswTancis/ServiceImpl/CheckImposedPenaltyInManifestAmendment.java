package com.ManifestTeswTancis.ServiceImpl;

import com.ManifestTeswTancis.Entity.*;
import com.ManifestTeswTancis.RabbitConfigurations.*;
import com.ManifestTeswTancis.Repository.*;
import com.ManifestTeswTancis.Response.BillItem;
import com.ManifestTeswTancis.Response.ManifestAmendmentBillNotice;
import com.ManifestTeswTancis.Util.ManifestAmendmentStatus;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Service
public class CheckImposedPenaltyInManifestAmendment {
    private static final Logger LOGGER = LoggerFactory.getLogger(CheckImposedPenaltyInManifestAmendment.class);
    @Value("${spring.rabbitmq.exchange.out}")
    private String OUTBOUND_EXCHANGE;
    @Value("http://192.168.30.200:7074/GetId")
    private String url;
    final MessageProducer rabbitMqMessageProducer;
    final ExImportAmendPenaltyRepository exImportAmendPenaltyRepository;
    final ManifestAmendmentApprovalStatusRepository manifestAmendmentApprovalStatusRepository;
    final QueueMessageStatusRepository queueMessageStatusRepository;
    final BillGeneralRepository billGeneralRepository;
    final BillGePGRepository billGePGRepository;
    final CoCompanyCodeRepository coCompanyCodeRepository;

    public CheckImposedPenaltyInManifestAmendment(MessageProducer rabbitMqMessageProducer, ExImportAmendPenaltyRepository exImportAmendPenaltyRepository, ManifestAmendmentApprovalStatusRepository manifestAmendmentApprovalStatusRepository, QueueMessageStatusRepository queueMessageStatusRepository, BillGeneralRepository billGeneralRepository, BillGePGRepository billGePGRepository, CoCompanyCodeRepository coCompanyCodeRepository) {
        this.rabbitMqMessageProducer = rabbitMqMessageProducer;
        this.exImportAmendPenaltyRepository = exImportAmendPenaltyRepository;
        this.manifestAmendmentApprovalStatusRepository = manifestAmendmentApprovalStatusRepository;
        this.queueMessageStatusRepository = queueMessageStatusRepository;
        this.billGeneralRepository = billGeneralRepository;
        this.billGePGRepository = billGePGRepository;
        this.coCompanyCodeRepository = coCompanyCodeRepository;
    }

    @Transactional
    @Scheduled(fixedRate = 500000)
    public void CheckImposedPenalty() {
        List<ManifestAmendmentApprovalStatus> status = manifestAmendmentApprovalStatusRepository.findByPenaltyImposedFalse();
        for (ManifestAmendmentApprovalStatus ma : status) {
            if (!ma.isPenaltyImposed()) {
                Optional<BillGeneralEntity>optional=billGeneralRepository.
                        findFirstByReferenceKeyOneAndReferenceKeyTwoAndReferenceKeyFour(ma.getDeclarantTin(), ma.getAmendYear(), ma.getAmendSerialNo());
                if (optional.isPresent()) {
                    BillGeneralEntity penalty=optional.get();
                    if (ManifestAmendmentStatus.RECEIVED.equals(penalty.getBillStatusCd())){
                    ManifestAmendmentBillNotice manifestAmendmentBillNotice = new ManifestAmendmentBillNotice();
                    List<BillItem> billItems = new ArrayList<>();
                    BillItem billItem = new BillItem();
                    manifestAmendmentBillNotice.setMsgRefNumb(ma.getAmendReference());
                    manifestAmendmentBillNotice.setBillReference(ma.getMrn());
                    manifestAmendmentBillNotice.setBillReferenceType("MANIFEST_AMENDMENT");
                    manifestAmendmentBillNotice.setGeneratedDate(penalty.getBillDate().toString());
                    Optional<CoCompanyCodeEntity> code = coCompanyCodeRepository.findByTin(penalty.getPayerTin());
                        if (code.isPresent()) {
                            CoCompanyCodeEntity companyCode= code.get();
                            manifestAmendmentBillNotice.setPayerInstitutionalCode(companyCode.getCompanyCode());
                        }else{
                            manifestAmendmentBillNotice.setPayerInstitutionalCode("Payer Institutional Code not Registered in TANCIS");
                        }
                    manifestAmendmentBillNotice.setPayeeInstitutionalCode("TRA");
                    manifestAmendmentBillNotice.setBillId(penalty.getBillRegisterId());
                    manifestAmendmentBillNotice.setBillAmount(penalty.getTotalBillTaxAmt());
                    manifestAmendmentBillNotice.setCcy("Tsh");
                    Optional<BillGePGEntity> bill=billGePGRepository.findByBillSerialNumberAndBillYear(penalty.getBillSerialNo(),penalty.getBillYy());
                    if(bill.isPresent()){
                        BillGePGEntity gepgControlNumber=bill.get();
                        manifestAmendmentBillNotice.setControlNumber(gepgControlNumber.getGepgControlNo());
                    }else{
                        Optional<ExImportAmendPenalty> opt=exImportAmendPenaltyRepository.findByDeclarantTin(penalty.getPayerTin());
                        if(opt.isPresent()){
                            ExImportAmendPenalty exImportAmendPenalty = opt.get();
                            manifestAmendmentBillNotice.setControlNumber(exImportAmendPenalty.getInvoiceNumber()+"/" + exImportAmendPenalty.getBillNumber());
                        }
                    }
                    manifestAmendmentBillNotice.setGeneratedBy("TRA");
                    manifestAmendmentBillNotice.setApprovedBy("TRA");
                    manifestAmendmentBillNotice.setApprovedDate(penalty.getBillDate().toString());
                    manifestAmendmentBillNotice.setDescription("PENALTY IMPOSED");
                    billItem.setBillAmount(penalty.getTotalBillTaxAmt());
                    billItem.setDescription("PENALTY IMPOSED");
                    billItem.setGfsCode("100126");
                    billItems.add(billItem);
                    manifestAmendmentBillNotice.setBillItems(billItems);
                    ma.setPenaltyImposed(true);
                    ma.setAmount(penalty.getTotalBillTaxAmt());
                    manifestAmendmentApprovalStatusRepository.save(ma);
                    String response = sendBillNoticeToQueue(manifestAmendmentBillNotice);
                    LOGGER.info("--Bill Notice--\n" + response);
                  }

                }
            }
        }
    }

    private String sendBillNoticeToQueue(ManifestAmendmentBillNotice manifestAmendmentBillNotice) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String payload = mapper.writeValueAsString(manifestAmendmentBillNotice);
            LOGGER.info("--Bill Notice--\n" + payload);
            MessageDto messageDto = new MessageDto();
            BillNoticeMessageDto billNoticeMessageDto = new BillNoticeMessageDto();
            billNoticeMessageDto.setMessageName(MessageNames.TESWS_BILL_NOTICE);
            RequestIdDto requestIdDto = mapper.readValue(getId(), RequestIdDto.class);
            billNoticeMessageDto.setRequestId(requestIdDto.getMessageId());
            messageDto.setPayload(manifestAmendmentBillNotice);
            AcknowledgementDto queueResponse = rabbitMqMessageProducer.
                    sendMessage(OUTBOUND_EXCHANGE, MessageNames.TESWS_BILL_NOTICE, billNoticeMessageDto.getRequestId(), messageDto.getCallbackUrl(), messageDto.getPayload());
            System.out.println(queueResponse);
            QueueMessageStatusEntity queueMessage = new QueueMessageStatusEntity();
            queueMessage.setMessageId(manifestAmendmentBillNotice.getBillId());
            queueMessage.setReferenceId(billNoticeMessageDto.getRequestId());
            queueMessage.setMessageName(MessageNames.TESWS_BILL_NOTICE);
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
        HttpGet request = new HttpGet(url);
        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = client.execute(request);
        HttpEntity entity = response.getEntity();
        return EntityUtils.toString(entity);
    }
}
