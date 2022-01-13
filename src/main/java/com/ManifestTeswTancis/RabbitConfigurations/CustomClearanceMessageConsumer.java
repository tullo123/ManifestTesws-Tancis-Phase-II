package com.ManifestTeswTancis.RabbitConfigurations;

import com.ManifestTeswTancis.ServiceImpl.CustomClearanceServiceImpl;
import com.ManifestTeswTancis.dtos.CustomClearanceDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class CustomClearanceMessageConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomClearanceMessageConsumer.class);
    private static final String MESSAGE_NAME = "CUSTOM_CLEARANCE_REQUEST";
    private static final String QUEUE_NAME = "q.tancis.in.custom-clearance-request";
    final ObjectMapper objectMapper;
    final CustomClearanceServiceImpl customClearanceServiceImpl;

    public CustomClearanceMessageConsumer(ObjectMapper objectMapper, CustomClearanceServiceImpl customClearanceServiceImpl) {
        this.objectMapper = objectMapper;
        this.customClearanceServiceImpl = customClearanceServiceImpl;
    }


    @RabbitListener(queues = QUEUE_NAME, containerFactory = "createListener")
    public void listen(String message) throws IOException {
        try {
            LOGGER.info("[CustomClearanceMessageConsumer.listen()] - [Start: {}]", MESSAGE_NAME);
            CustomClearanceMessageDto customClearanceMessageDto = objectMapper.readValue(message, CustomClearanceMessageDto.class);
            LOGGER.info("CustomClearanceMessageConsumer.listen()] - [CustomClearanceMessageConsumer: {}]", customClearanceMessageDto);
            CustomClearanceDto customClearanceDto = customClearanceMessageDto.getPayload();
            System.out.println(customClearanceDto);
            customClearanceServiceImpl.customService(customClearanceDto);
            LOGGER.info("[CustomClearanceMessageConsumer.listen()] - [End: {}]", MESSAGE_NAME);
            TimeUnit.SECONDS.sleep(5);
        } catch (IOException ioe) {
            LOGGER.error("[CustomClearanceMessageConsumer.listen()] - [IO Exception: {}]", ioe.getMessage());
            throw new IOException(ioe.getMessage());
        } catch (InterruptedException ine) {
            LOGGER.error("[CustomClearanceMessageConsumer.listen()] - [Interrupted Exception: {}]", ine.getMessage());
        }
    }
}