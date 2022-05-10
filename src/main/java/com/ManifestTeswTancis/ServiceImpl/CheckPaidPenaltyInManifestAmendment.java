package com.ManifestTeswTancis.ServiceImpl;

import com.ManifestTeswTancis.Entity.*;
import com.ManifestTeswTancis.RabbitConfigurations.*;
import com.ManifestTeswTancis.Repository.*;
import com.ManifestTeswTancis.Response.ManifestAmendmentPaymentNotice;
import com.ManifestTeswTancis.Response.Payment;
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
public class CheckPaidPenaltyInManifestAmendment {
    private static final Logger LOGGER = LoggerFactory.getLogger(CheckPaidPenaltyInManifestAmendment.class);
    @Value("${spring.rabbitmq.exchange.out}")
    private String OUTBOUND_EXCHANGE;
    @Value("${url}")
    private String url;
    final MessageProducer rabbitMqMessageProducer;
    final ManifestAmendmentApprovalStatusRepository manifestAmendmentApprovalStatusRepository;
    final QueueMessageStatusRepository queueMessageStatusRepository;
    final BillGeneralRepository billGeneralRepository;
    final BillGePGRepository billGePGRepository;
    final ExImportAmendPenaltyRepository exImportAmendPenaltyRepository;

    public CheckPaidPenaltyInManifestAmendment(MessageProducer rabbitMqMessageProducer, ManifestAmendmentApprovalStatusRepository manifestAmendmentApprovalStatusRepository, QueueMessageStatusRepository queueMessageStatusRepository, BillGeneralRepository billGeneralRepository, BillGePGRepository billGePGRepository, ExImportAmendPenaltyRepository exImportAmendPenaltyRepository) {
        this.rabbitMqMessageProducer = rabbitMqMessageProducer;
        this.manifestAmendmentApprovalStatusRepository = manifestAmendmentApprovalStatusRepository;
        this.queueMessageStatusRepository = queueMessageStatusRepository;
        this.billGeneralRepository = billGeneralRepository;
        this.billGePGRepository = billGePGRepository;
        this.exImportAmendPenaltyRepository = exImportAmendPenaltyRepository;
    }
    @Transactional
    @Scheduled(fixedRate = 600000)
    public void CheckPidPenalty(){
        List<ManifestAmendmentApprovalStatus> status = manifestAmendmentApprovalStatusRepository.findByPenaltyPaidFalse();
        for (ManifestAmendmentApprovalStatus ma : status) {
            if(!ma.isPenaltyPaid()){
                Optional<BillGeneralEntity> optional=billGeneralRepository.
                        findFirstByReferenceKeyOneAndReferenceKeyTwoAndReferenceKeyFour(ma.getDeclarantTin(), ma.getAmendYear(), ma.getAmendSerialNo());
                if (optional.isPresent()) {
                    BillGeneralEntity penalty=optional.get();
                    if (ManifestAmendmentStatus.PAID.equals(penalty.getBillStatusCd())){
                        ManifestAmendmentPaymentNotice manifestAmendmentPaymentNotice = new ManifestAmendmentPaymentNotice();
                        List<Payment> payment = new ArrayList<>();
                        Payment payments = new Payment();
                        manifestAmendmentPaymentNotice.setMsgRefNumb(ma.getAmendReference());
                        manifestAmendmentPaymentNotice.setPaymentDate(penalty.getBillDate().toString());
                        manifestAmendmentPaymentNotice.setBillId(penalty.getBillRegisterId());
                        Optional<BillGePGEntity> bill=billGePGRepository.findByBillSerialNumberAndBillYear(penalty.getBillSerialNo(),penalty.getBillYy());
                        if(bill.isPresent()){
                            BillGePGEntity gepgControlNumber=bill.get();
                            manifestAmendmentPaymentNotice.setControlNumber(gepgControlNumber.getGepgControlNo());
                        }else{
                            Optional<ExImportAmendPenalty> opt=exImportAmendPenaltyRepository.
                                    findFirstByDeclarantTinAndAmendYearAndAmendSerialNumber(ma.getDeclarantTin(),ma.getAmendYear(),ma.getAmendSerialNo());
                            if(opt.isPresent()){
                                ExImportAmendPenalty exImportAmendPenalty = opt.get();
                                manifestAmendmentPaymentNotice.setControlNumber(exImportAmendPenalty.getInvoiceNumber()+"/" + exImportAmendPenalty.getBillNumber());
                            }
                        }
                        manifestAmendmentPaymentNotice.setPayerName(penalty.getPayerName());
                        manifestAmendmentPaymentNotice.setPayerPhoneNumber(penalty.getPayerTin());
                        payments.setReceiptNumber(penalty.getReceiptNo());
                        payments.setPaidAmount(penalty.getTotalBillTaxAmt());
                        payments.setCcy("Tsh");
                        payment.add(payments);
                        manifestAmendmentPaymentNotice.setPayment(payment);
                        ma.setPenaltyPaid(true);
                        manifestAmendmentApprovalStatusRepository.save(ma);
                        String response = sendPaymentNoticeToQueue(manifestAmendmentPaymentNotice);
                        LOGGER.info("--Payment Notice--\n" + response);
                    }
                }
            }
        }
    }

    private String sendPaymentNoticeToQueue(ManifestAmendmentPaymentNotice manifestAmendmentPaymentNotice) {
        ObjectMapper mapper = new ObjectMapper();
        try{
            String payload = mapper.writeValueAsString(manifestAmendmentPaymentNotice);
            LOGGER.info("--Payment Notice--\n" + payload);
            MessageDto messageDto = new MessageDto();
            PaymentNoticeMessageDto paymentNoticeMessageDto = new PaymentNoticeMessageDto();
            paymentNoticeMessageDto.setMessageName(MessageNames.TESWS_PAYMENT_NOTICE);
            RequestIdDto requestIdDto = mapper.readValue(getId(), RequestIdDto.class);
            paymentNoticeMessageDto.setRequestId(requestIdDto.getMessageId());
            messageDto.setPayload(manifestAmendmentPaymentNotice);
            AcknowledgementDto queueResponse = rabbitMqMessageProducer.
                    sendMessage(OUTBOUND_EXCHANGE, MessageNames.TESWS_PAYMENT_NOTICE, paymentNoticeMessageDto.getRequestId(), messageDto.getCallbackUrl(), messageDto.getPayload());
            LOGGER.info("---RabbitMQ Response---\n"+queueResponse);
            QueueMessageStatusEntity queueMessage = new QueueMessageStatusEntity();
            queueMessage.setMessageId(manifestAmendmentPaymentNotice.getBillId());
            queueMessage.setReferenceId(paymentNoticeMessageDto.getRequestId());
            queueMessage.setMessageName(MessageNames.TESWS_PAYMENT_NOTICE);
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
