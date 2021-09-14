package com.ManifestTeswTancis.RabbitConfigurations;

import com.ManifestTeswTancis.ServiceImpl.VesselBoardingServiceImpl;
import com.ManifestTeswTancis.dtos.VesselBoardingNotificationDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class VesselBoardingNotificationMessageConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(VesselBoardingNotificationMessageConsumer.class);
    private static final String MESSAGE_NAME = "VESSEL_BOARDING_NOTIFICATION";
    private static final String QUEUE_NAME = "q.tancis.in.vessel-boarding-notification";
    final ObjectMapper objectMapper;
    final VesselBoardingServiceImpl vesselBoardingServiceImpl;

    public VesselBoardingNotificationMessageConsumer(ObjectMapper objectMapper, VesselBoardingServiceImpl vesselBoardingServiceImpl) {
        this.objectMapper = objectMapper;
        this.vesselBoardingServiceImpl = vesselBoardingServiceImpl;
    }
    @RabbitListener(queues = QUEUE_NAME, containerFactory = "createListener")
    public void listen(String message) throws IOException {
        try {
            LOGGER.info("[VesselBoardingNotificationMessageConsumer.listen()] - [Start: {}]", MESSAGE_NAME);
            VesselBoardingNotificationMessageDto vesselBoardingNotificationMessageDto = objectMapper.readValue(message, VesselBoardingNotificationMessageDto.class);
            LOGGER.info("VesselBoardingNotificationMessageConsumer.listen()] - [VesselBoardingNotificationMessageConsumer: {}]", vesselBoardingNotificationMessageDto);
            VesselBoardingNotificationDto vesselBoardingNotificationDto = vesselBoardingNotificationMessageDto.getPayload();
            System.out.println(vesselBoardingNotificationDto);
            vesselBoardingServiceImpl.saveVesselBoarding(vesselBoardingNotificationDto);
            LOGGER.info("[VesselBoardingNotificationMessageConsumer.listen()] - [End: {}]", MESSAGE_NAME);
            TimeUnit.SECONDS.sleep(5);
        } catch (IOException ioe) {
            LOGGER.error("[VesselBoardingNotificationMessageConsumer.listen()] - [IO Exception: {}]", ioe.getMessage());
            throw new IOException(ioe.getMessage());
        } catch (InterruptedException ine) {
            LOGGER.error("[VesselBoardingNotificationMessageConsumer.listen()] - [Interrupted Exception: {}]", ine.getMessage());
        }
    }
}
