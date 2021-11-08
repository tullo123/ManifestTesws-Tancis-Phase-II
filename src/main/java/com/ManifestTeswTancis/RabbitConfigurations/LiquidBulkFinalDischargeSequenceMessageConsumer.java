package com.ManifestTeswTancis.RabbitConfigurations;

import com.ManifestTeswTancis.ServiceImpl.LiquidBulkFinalDischargeServiceImpl;
import com.ManifestTeswTancis.dtos.LiquidBulkDischargeSequenceUpdateDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class LiquidBulkFinalDischargeSequenceMessageConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(LiquidBulkFinalDischargeSequenceMessageConsumer.class);
    private static final String MESSAGE_NAME = "LIQUID_BULK_FINAL_DISCHARGE_SEQUENCE";
    private static final String QUEUE_NAME = "q.tancis.in.liquid-bulk-final-discharge-sequence";
    final ObjectMapper objectMapper;
    final LiquidBulkFinalDischargeServiceImpl liquidBulkFinalDischargeServiceImpl;

    public LiquidBulkFinalDischargeSequenceMessageConsumer(ObjectMapper objectMapper, LiquidBulkFinalDischargeServiceImpl liquidBulkFinalDischargeServiceImpl) {
        this.objectMapper = objectMapper;
        this.liquidBulkFinalDischargeServiceImpl = liquidBulkFinalDischargeServiceImpl;
    }
    @RabbitListener(queues = QUEUE_NAME, containerFactory = "createListener")
    public void listen(String message) throws IOException {
        try {
            LOGGER.info("[LiquidBulkFinalDischargeSequenceMessageConsumer.listen()] - [Start: {}]", MESSAGE_NAME);
            LiquidBulkFinalDischargeSequenceMessageDto liquidBulkFinalDischargeSequenceMessageDto = objectMapper.readValue(message, LiquidBulkFinalDischargeSequenceMessageDto.class);
            LOGGER.info("LiquidBulkFinalDischargeSequenceMessageConsumer.listen()] - [LiquidBulkFinalDischargeSequenceMessageConsumer: {}]", liquidBulkFinalDischargeSequenceMessageDto);
            LiquidBulkDischargeSequenceUpdateDto liquidBulkDischargeSequenceUpdateDto = liquidBulkFinalDischargeSequenceMessageDto.getPayload();
            System.out.println(liquidBulkDischargeSequenceUpdateDto);
            liquidBulkFinalDischargeServiceImpl.finalDischargeSequence(liquidBulkDischargeSequenceUpdateDto);
            LOGGER.info("[LiquidBulkFinalDischargeSequenceMessageConsumer.listen()] - [End: {}]", MESSAGE_NAME);
            TimeUnit.SECONDS.sleep(5);
        } catch (IOException ioe) {
            LOGGER.error("[LiquidBulkFinalDischargeSequenceMessageConsumer.listen()] - [IO Exception: {}]", ioe.getMessage());
            throw new IOException(ioe.getMessage());
        } catch (InterruptedException ine) {
            LOGGER.error("[LiquidBulkFinalDischargeSequenceMessageConsumer.listen()] - [Interrupted Exception: {}]", ine.getMessage());
        }
    }
}
