package com.ManifestTeswTancis.RabbitConfigurations;

import com.ManifestTeswTancis.ServiceImpl.VesselDocumentationUpdateServiceImpl;
import com.ManifestTeswTancis.dtos.VesselDocumentationDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class VesselDocumentationUpdateMessageConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(VesselDocumentationUpdateMessageConsumer.class);
    private static final String MESSAGE_NAME = "VESSEL_DOCUMENTATION_UPDATE";
    private static final String QUEUE_NAME = "q.tancis.in.vessel-documentation-update";
    final ObjectMapper objectMapper;
    final VesselDocumentationUpdateServiceImpl vesselDocumentationUpdateServiceImpl;

    public VesselDocumentationUpdateMessageConsumer(ObjectMapper objectMapper, VesselDocumentationUpdateServiceImpl vesselDocumentationUpdateServiceImpl) {
        this.objectMapper = objectMapper;
        this.vesselDocumentationUpdateServiceImpl = vesselDocumentationUpdateServiceImpl;
    }
    @RabbitListener(queues = QUEUE_NAME, containerFactory = "createListener")
    public void listen(String message) throws IOException {
        try {
            LOGGER.info("[VesselDocumentationUpdateMessageConsumer.listen()] - [Start: {}]", MESSAGE_NAME);
            VesselDocumentationUpdateMessageDto vesselDocumentationUpdateMessageDto = objectMapper.readValue(message, VesselDocumentationUpdateMessageDto.class);
            LOGGER.info("VesselDocumentationUpdateMessageDto.listen()] - [VesselDocumentationUpdateMessageDto: {}]", vesselDocumentationUpdateMessageDto);
            VesselDocumentationDto vesselDocumentationDto = vesselDocumentationUpdateMessageDto.getPayload();
            System.out.println(vesselDocumentationDto);
            vesselDocumentationUpdateServiceImpl.vesselDocumentationUpdate(vesselDocumentationDto);
            LOGGER.info("[VesselDocumentationUpdateMessageConsumer.listen()] - [End: {}]", MESSAGE_NAME);
            TimeUnit.SECONDS.sleep(5);
        } catch (IOException ioe) {
            LOGGER.error("[VesselDocumentationUpdateMessageConsumer.listen()] - [IO Exception: {}]", ioe.getMessage());
            throw new IOException(ioe.getMessage());
        } catch (InterruptedException ine) {
            LOGGER.error("[VesselDocumentationUpdateMessageConsumer.listen()] - [Interrupted Exception: {}]", ine.getMessage());
        }
    }
}
