package com.ManifestTeswTancis.RabbitConfigurations;

import com.ManifestTeswTancis.ServiceImpl.LiquidBulkQualityReportServiceImpl;
import com.ManifestTeswTancis.dtos.LiquidBulkQualityReportDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class LiquidBulkQualityReportMessageConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(LiquidBulkQualityReportMessageConsumer.class);
    private static final String MESSAGE_NAME = "LIQUID_BULK_QUALITY_REPORT";
    private static final String QUEUE_NAME = "q.tancis.in.liquid-bulk-quality-report";
    final ObjectMapper objectMapper;
    final LiquidBulkQualityReportServiceImpl liquidBulkQualityReportServiceImpl;

    public LiquidBulkQualityReportMessageConsumer(ObjectMapper objectMapper, LiquidBulkQualityReportServiceImpl liquidBulkQualityReportServiceImpl) {
        this.objectMapper = objectMapper;
        this.liquidBulkQualityReportServiceImpl = liquidBulkQualityReportServiceImpl;
    }
    @RabbitListener(queues = QUEUE_NAME, containerFactory = "createListener")
    public void listen(String message) throws IOException {
        try {
            LOGGER.info("[LiquidBulkQualityReportMessageConsumer.listen()] - [Start: {}]", MESSAGE_NAME);
            LiquidBulkQualityReportMessageDto liquidBulkQualityReportMessageDto = objectMapper.readValue(message, LiquidBulkQualityReportMessageDto.class);
            LOGGER.info("LiquidBulkQualityReportMessageConsumer.listen()] - [LiquidBulkQualityReportMessageConsumer: {}]", liquidBulkQualityReportMessageDto);
            LiquidBulkQualityReportDto liquidBulkQualityReportDto = liquidBulkQualityReportMessageDto.getPayload();
            System.out.println(liquidBulkQualityReportDto);
            liquidBulkQualityReportServiceImpl.saveLiquidBulkQualityReport(liquidBulkQualityReportDto);
            LOGGER.info("[LiquidBulkQualityReportMessageConsumer.listen()] - [End: {}]", MESSAGE_NAME);
            TimeUnit.SECONDS.sleep(5);
        } catch (IOException ioe) {
            LOGGER.error("[LiquidBulkQualityReportMessageConsumer.listen()] - [IO Exception: {}]", ioe.getMessage());
            throw new IOException(ioe.getMessage());
        } catch (InterruptedException ine) {
            LOGGER.error("[LiquidBulkQualityReportMessageConsumer.listen()] - [Interrupted Exception: {}]", ine.getMessage());
        }
    }
}
