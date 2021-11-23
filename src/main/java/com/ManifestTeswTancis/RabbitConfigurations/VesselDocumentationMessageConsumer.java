package com.ManifestTeswTancis.RabbitConfigurations;

import com.ManifestTeswTancis.ServiceImpl.VesselDocumentationServiceImpl;
import com.ManifestTeswTancis.dtos.VesselDocumentationDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class VesselDocumentationMessageConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(VesselDocumentationMessageConsumer.class);
    private static final String MESSAGE_NAME = "VESSEL_DOCUMENTATION";
    private static final String QUEUE_NAME = "q.tancis.in.vessel-documentation";
    final ObjectMapper objectMapper;
    final VesselDocumentationServiceImpl vesselDocumentationServiceImpl;

    public VesselDocumentationMessageConsumer(ObjectMapper objectMapper, VesselDocumentationServiceImpl vesselDocumentationServiceImpl) {
        this.objectMapper = objectMapper;
        this.vesselDocumentationServiceImpl = vesselDocumentationServiceImpl;
    }
    @RabbitListener(queues = QUEUE_NAME, containerFactory = "createListener")
    public void listen(String message) throws IOException {
        try {
            LOGGER.info("[VesselDocumentationMessageConsumer.listen()] - [Start: {}]", MESSAGE_NAME);
            VesselDocumentationMessageDto vesselDocumentationMessageDto = objectMapper.readValue(message, VesselDocumentationMessageDto.class);
            LOGGER.info("VesselDocumentationMessageConsumer.listen()] - [VesselDocumentationMessageConsumer: {}]", vesselDocumentationMessageDto);
            VesselDocumentationDto vesselDocumentationDto = vesselDocumentationMessageDto.getPayload();
            System.out.println(vesselDocumentationDto);
            vesselDocumentationServiceImpl.vesselDocumentationReceiving(vesselDocumentationDto);
            LOGGER.info("[VesselDocumentationMessageConsumer.listen()] - [End: {}]", MESSAGE_NAME);
            TimeUnit.SECONDS.sleep(5);
        } catch (IOException ioe) {
            LOGGER.error("[VesselDocumentationMessageConsumer.listen()] - [IO Exception: {}]", ioe.getMessage());
            throw new IOException(ioe.getMessage());
        } catch (InterruptedException ine) {
            LOGGER.error("[VesselDocumentationMessageConsumer.listen()] - [Interrupted Exception: {}]", ine.getMessage());
        }
    }
}
