package com.ManifestTeswTancis.RabbitConfigurations;

import com.ManifestTeswTancis.ServiceImpl.VesselTrackingServiceImpl;
import com.ManifestTeswTancis.dtos.VesselTrackingNoticeDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class VesselTrackingMessageConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(VesselTrackingMessageConsumer.class);
    private static final String MESSAGE_NAME = "VESSEL_TRACKING_NOTICE";
    private static final String QUEUE_NAME = "q.tancis.in.vessel-tracking-notice";
    final ObjectMapper objectMapper;
    final VesselTrackingServiceImpl vesselTrackingServiceImpl;

    public VesselTrackingMessageConsumer(ObjectMapper objectMapper, VesselTrackingServiceImpl vesselTrackingServiceImpl) {
        this.objectMapper = objectMapper;
        this.vesselTrackingServiceImpl = vesselTrackingServiceImpl;
    }
    @RabbitListener(queues = QUEUE_NAME, containerFactory = "createListener")
    public void listen(String message) throws IOException {
        try {
            LOGGER.info("[VesselTrackingMessageConsumer.listen()] - [Start: {}]", MESSAGE_NAME);
            VesselTrackingMessageNoticeDto vesselTrackingMessageNoticeDto = objectMapper.readValue(message, VesselTrackingMessageNoticeDto.class);
            LOGGER.info("VesselTrackingMessageConsumer.listen()] - [VesselTrackingMessageConsumer: {}]", vesselTrackingMessageNoticeDto);
            VesselTrackingNoticeDto vesselTrackingNoticeDto = vesselTrackingMessageNoticeDto.getPayload();
            System.out.println(vesselTrackingNoticeDto);
            vesselTrackingServiceImpl.vesselTracking(vesselTrackingNoticeDto);
            LOGGER.info("[VesselTrackingMessageConsumer.listen()] - [End: {}]", MESSAGE_NAME);
            TimeUnit.SECONDS.sleep(5);
        } catch (IOException ioe) {
            LOGGER.error("[VesselTrackingMessageConsumer.listen()] - [IO Exception: {}]", ioe.getMessage());
            throw new IOException(ioe.getMessage());
        } catch (InterruptedException ine) {
            LOGGER.error("[VesselTrackingMessageConsumer.listen()] - [Interrupted Exception: {}]", ine.getMessage());
        }
    }
}
