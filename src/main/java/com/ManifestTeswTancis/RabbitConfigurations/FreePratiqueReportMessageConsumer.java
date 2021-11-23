package com.ManifestTeswTancis.RabbitConfigurations;

import com.ManifestTeswTancis.ServiceImpl.FreePartiqueServiceImpl;
import com.ManifestTeswTancis.dtos.FreePratiqueReportDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class FreePratiqueReportMessageConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(FreePratiqueReportMessageConsumer.class);
    private static final String MESSAGE_NAME = "FREE_PRATIQUE_REPORT";
    private static final String QUEUE_NAME = "q.tancis.in.free-pratique-report";
     final ObjectMapper objectMapper;
     final FreePartiqueServiceImpl freePartiqueServiceImpl;

    public FreePratiqueReportMessageConsumer(ObjectMapper objectMapper, FreePartiqueServiceImpl freePartiqueServiceImpl) {
        this.objectMapper = objectMapper;
        this.freePartiqueServiceImpl = freePartiqueServiceImpl;
    }
    @RabbitListener(queues = QUEUE_NAME, containerFactory = "createListener")
    public void listen(String message) throws IOException {
        try {
            LOGGER.info("[FreePratiqueReportMessageConsumer.listen()] - [Start: {}]", MESSAGE_NAME);
            FreePratiqueReportMessageDto freePratiqueReportMessageDto = objectMapper.readValue(message, FreePratiqueReportMessageDto.class);
            LOGGER.info("FreePratiqueReportMessageConsumer.listen()] - [FreePratiqueReportMessageConsumer: {}]", freePratiqueReportMessageDto);
            FreePratiqueReportDto freePratiqueReportDto = freePratiqueReportMessageDto.getPayload();
            System.out.println(freePratiqueReportDto);
            freePartiqueServiceImpl.freePartique(freePratiqueReportDto);
            LOGGER.info("[FreePratiqueReportMessageConsumer.listen()] - [End: {}]", MESSAGE_NAME);
            TimeUnit.SECONDS.sleep(5);
        } catch (IOException ioe) {
            LOGGER.error("[FreePratiqueReportMessageConsumer.listen()] - [IO Exception: {}]", ioe.getMessage());
            throw new IOException(ioe.getMessage());
        } catch (InterruptedException ine) {
            LOGGER.error("[FreePratiqueReportMessageConsumer.listen()] - [Interrupted Exception: {}]", ine.getMessage());
        }
    }
}
