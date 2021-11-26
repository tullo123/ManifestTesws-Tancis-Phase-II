package com.ManifestTeswTancis.RabbitConfigurations;

import com.ManifestTeswTancis.ServiceImpl.LiquidBulkDischargeSequenceServiceImpl;
import com.ManifestTeswTancis.dtos.LiquidBulkDischargeSequenceDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class LiquidBulkDischargeSequenceMessageConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(LiquidBulkDischargeSequenceMessageConsumer.class);
    private static final String MESSAGE_NAME = "LIQUID_BULK_DISCHARGE_SEQUENCE";
    private static final String QUEUE_NAME = "q.tancis.in.liquid-bulk-discharge-sequence";
    final ObjectMapper objectMapper;
    final LiquidBulkDischargeSequenceServiceImpl liquidBulkDischargeSequenceServiceImpl;

    public LiquidBulkDischargeSequenceMessageConsumer(ObjectMapper objectMapper, LiquidBulkDischargeSequenceServiceImpl liquidBulkDischargeSequenceServiceImpl) {
        this.objectMapper = objectMapper;
        this.liquidBulkDischargeSequenceServiceImpl = liquidBulkDischargeSequenceServiceImpl;
    }
    @RabbitListener(queues = QUEUE_NAME, containerFactory = "createListener")
    public void listen(String message) throws IOException {
        try {
            LOGGER.info("[LiquidBulkDischargeSequenceMessageConsumer.listen()] - [Start: {}]", MESSAGE_NAME);
            LiquidBulkDischargeSequenceMessageDto liquidBulkDischargeSequenceMessageDto = objectMapper.readValue(message, LiquidBulkDischargeSequenceMessageDto.class);
            LOGGER.info("LiquidBulkDischargeSequenceMessageConsumer.listen()] - [LiquidBulkDischargeSequenceMessageConsumer: {}]", liquidBulkDischargeSequenceMessageDto);
            LiquidBulkDischargeSequenceDto liquidBulkDischargeSequenceDto = liquidBulkDischargeSequenceMessageDto.getPayload();
            System.out.println(liquidBulkDischargeSequenceDto);
            liquidBulkDischargeSequenceServiceImpl.saveLiquidBulkDischargeSequence(liquidBulkDischargeSequenceDto);
            LOGGER.info("[LiquidBulkDischargeSequenceMessageConsumer.listen()] - [End: {}]", MESSAGE_NAME);
            TimeUnit.SECONDS.sleep(5);
        } catch (IOException ioe) {
            LOGGER.error("[LiquidBulkDischargeSequenceMessageConsumer.listen()] - [IO Exception: {}]", ioe.getMessage());
            throw new IOException(ioe.getMessage());
        } catch (InterruptedException ine) {
            LOGGER.error("[LiquidBulkDischargeSequenceMessageConsumer.listen()] - [Interrupted Exception: {}]", ine.getMessage());
        }
    }
}
