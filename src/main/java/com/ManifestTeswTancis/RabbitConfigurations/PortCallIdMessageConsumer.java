package com.ManifestTeswTancis.RabbitConfigurations;

import com.ManifestTeswTancis.Request.CallInfDetailsRequestModel;
import com.ManifestTeswTancis.ServiceImpl.CallInfServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class PortCallIdMessageConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(PortCallIdMessageConsumer.class);
    private static final String MESSAGE_NAME = "PORT_CALL_ID";
    private static final String QUEUE_NAME = "q.tancis.in.port-call-id";
    final ObjectMapper objectMapper;
    final CallInfServiceImpl callInfServiceImpl;

    public PortCallIdMessageConsumer(ObjectMapper objectMapper, CallInfServiceImpl callInfServiceImpl) {
        this.objectMapper = objectMapper;
        this.callInfServiceImpl = callInfServiceImpl;
    }
    @RabbitListener(queues = QUEUE_NAME, containerFactory = "createListener")
    public void listen(String message) throws IOException {
        try {
            LOGGER.info("[PortCallIdMessageConsumer.listen()] - [Start: {}]", MESSAGE_NAME);
            PortCallIdMessageDto portCallIdMessageDto = objectMapper.readValue(message, PortCallIdMessageDto.class);
            LOGGER.info("PortCallIdMessageConsumer.listen()] - [PortCallIdMessageConsumer: {}]", portCallIdMessageDto);
            CallInfDetailsRequestModel callInfDetailsRequestModel = portCallIdMessageDto.getPayload();
            System.out.println(callInfDetailsRequestModel);
            callInfServiceImpl.createCallInfo(callInfDetailsRequestModel);
            LOGGER.info("[PortCallIdMessageConsumer.listen()] - [End: {}]", MESSAGE_NAME);
            TimeUnit.SECONDS.sleep(5);
        } catch (IOException ioe) {
            LOGGER.error("[PortCallIdMessageConsumer.listen()] - [IO Exception: {}]", ioe.getMessage());
            throw new IOException(ioe.getMessage());
        } catch (InterruptedException ine) {
            LOGGER.error("[PortCallIdMessageConsumer.listen()] - [Interrupted Exception: {}]", ine.getMessage());
        }
    }
}
