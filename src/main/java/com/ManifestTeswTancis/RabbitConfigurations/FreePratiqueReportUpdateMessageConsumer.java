package com.ManifestTeswTancis.RabbitConfigurations;

import com.ManifestTeswTancis.ServiceImpl.UpdateFreePartiqueServiceImpl;
import com.ManifestTeswTancis.dtos.FreePratiqueReportDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class FreePratiqueReportUpdateMessageConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(FreePratiqueReportUpdateMessageConsumer.class);
    private static final String MESSAGE_NAME = "FREE_PRATIQUE_REPORT_UPDATE";
    private static final String QUEUE_NAME = "q.tancis.in.free-pratique-report-update";
    final ObjectMapper objectMapper;
    final UpdateFreePartiqueServiceImpl updateFreePartiqueServiceImpl;

    public FreePratiqueReportUpdateMessageConsumer(ObjectMapper objectMapper, UpdateFreePartiqueServiceImpl updateFreePartiqueServiceImpl) {
        this.objectMapper = objectMapper;
        this.updateFreePartiqueServiceImpl = updateFreePartiqueServiceImpl;
    }
    @RabbitListener(queues = QUEUE_NAME, containerFactory = "createListener")
    public void listen(String message) throws IOException {
        try {
            LOGGER.info("[FreePratiqueReportUpdateMessageConsumer.listen()] - [Start: {}]", MESSAGE_NAME);
            FreePratiqueReportUpdateMessageDto freePratiqueReportUpdateMessageDto = objectMapper.readValue(message, FreePratiqueReportUpdateMessageDto.class);
            LOGGER.info("FreePratiqueReportUpdateMessageConsumer.listen()] - [FreePratiqueReportUpdateMessageConsumer: {}]", freePratiqueReportUpdateMessageDto);
            FreePratiqueReportDto freePratiqueReportDto = freePratiqueReportUpdateMessageDto.getPayload();
            System.out.println(freePratiqueReportDto);
            updateFreePartiqueServiceImpl.updateFreePartiqueReport(freePratiqueReportDto);
            LOGGER.info("[FreePratiqueReportUpdateMessageConsumer.listen()] - [End: {}]", MESSAGE_NAME);
            TimeUnit.SECONDS.sleep(5);
        } catch (IOException ioe) {
            LOGGER.error("[FreePratiqueReportUpdateMessageConsumer.listen()] - [IO Exception: {}]", ioe.getMessage());
            throw new IOException(ioe.getMessage());
        } catch (InterruptedException ine) {
            LOGGER.error("[FreePratiqueReportUpdateMessageConsumer.listen()] - [Interrupted Exception: {}]", ine.getMessage());
        }
    }
}
