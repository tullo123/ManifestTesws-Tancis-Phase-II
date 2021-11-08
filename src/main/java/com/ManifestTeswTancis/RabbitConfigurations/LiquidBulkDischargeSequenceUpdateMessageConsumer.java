package com.ManifestTeswTancis.RabbitConfigurations;

import com.ManifestTeswTancis.ServiceImpl.LiquidBulkDischargeSequenceUpdateServiceImpl;
import com.ManifestTeswTancis.dtos.LiquidBulkDischargeSequenceUpdateDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class LiquidBulkDischargeSequenceUpdateMessageConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(LiquidBulkDischargeSequenceUpdateMessageConsumer.class);
    private static final String MESSAGE_NAME = "LIQUID_BULK_DISCHARGE_SEQUENCE_UPDATE";
    private static final String QUEUE_NAME = "q.tancis.in.liquid-bulk-discharge-sequence-update";
    final ObjectMapper objectMapper;
    final LiquidBulkDischargeSequenceUpdateServiceImpl liquidBulkDischargeSequenceUpdateServiceImpl;

    public LiquidBulkDischargeSequenceUpdateMessageConsumer(ObjectMapper objectMapper, LiquidBulkDischargeSequenceUpdateServiceImpl liquidBulkDischargeSequenceUpdateServiceImpl) {
        this.objectMapper = objectMapper;
        this.liquidBulkDischargeSequenceUpdateServiceImpl = liquidBulkDischargeSequenceUpdateServiceImpl;
    }
    @RabbitListener(queues = QUEUE_NAME, containerFactory = "createListener")
    public void listen(String message) throws IOException {
        try {
            LOGGER.info("[LiquidBulkDischargeSequenceUpdateMessageConsumer.listen()] - [Start: {}]", MESSAGE_NAME);
            LiquidBulkDischargeSequenceUpdateMessageDto liquidBulkDischargeSequenceUpdateMessageDto = objectMapper.readValue(message, LiquidBulkDischargeSequenceUpdateMessageDto.class);
            LOGGER.info("LiquidBulkDischargeSequenceUpdateMessageConsumer.listen()] - [LiquidBulkDischargeSequenceUpdateMessageConsumer: {}]", liquidBulkDischargeSequenceUpdateMessageDto);
            LiquidBulkDischargeSequenceUpdateDto liquidBulkDischargeSequenceUpdateDto = liquidBulkDischargeSequenceUpdateMessageDto.getPayload();
            System.out.println(liquidBulkDischargeSequenceUpdateDto);
            liquidBulkDischargeSequenceUpdateServiceImpl.saveLiquidBulkDischargeSequenceUpdate(liquidBulkDischargeSequenceUpdateDto);
            LOGGER.info("[LiquidBulkDischargeSequenceUpdateMessageConsumer.listen()] - [End: {}]", MESSAGE_NAME);
            TimeUnit.SECONDS.sleep(5);
        } catch (IOException ioe) {
            LOGGER.error("[LiquidBulkDischargeSequenceUpdateMessageConsumer.listen()] - [IO Exception: {}]", ioe.getMessage());
            throw new IOException(ioe.getMessage());
        } catch (InterruptedException ine) {
            LOGGER.error("[LiquidBulkDischargeSequenceUpdateMessageConsumer.listen()] - [Interrupted Exception: {}]", ine.getMessage());
        }
    }
}
