package com.ManifestTeswTancis.RabbitConfigurations;

import com.ManifestTeswTancis.ServiceImpl.VesselDepartureServiceImpl;
import com.ManifestTeswTancis.dtos.VesselDepartureNoticeDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class VesselDepartureMessageConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(VesselDepartureMessageConsumer.class);
    private static final String MESSAGE_NAME = "VESSEL_DEPARTURE_NOTICE";
    private static final String QUEUE_NAME = "q.tancis.in.vessel-departure-notice";
    private final ObjectMapper objectMapper;
    final VesselDepartureServiceImpl vesselDepartureServiceImpl;

    public VesselDepartureMessageConsumer(ObjectMapper objectMapper, VesselDepartureServiceImpl vesselDepartureServiceImpl) {
        this.objectMapper = objectMapper;
        this.vesselDepartureServiceImpl = vesselDepartureServiceImpl;
    }

    @RabbitListener(queues = QUEUE_NAME, containerFactory = "createListener")
    public void listen(String message) throws IOException {
        try {
            LOGGER.info("[VesselDepartureMessageConsumer.listen()] - [Start: {}]", MESSAGE_NAME);
            VesselDepartureMessageDto vesselDepartureMessageDto = objectMapper.readValue(message, VesselDepartureMessageDto.class);
            LOGGER.info("VesselDepartureMessageConsumer.listen()] - [vesselDepartureMessageConsumer: {}]", vesselDepartureMessageDto);
            VesselDepartureNoticeDto vesselDepartureNoticeDto = vesselDepartureMessageDto.getPayload();
            System.out.println(vesselDepartureNoticeDto);
            vesselDepartureServiceImpl.saveVesselDepartureNotice(vesselDepartureNoticeDto);
            LOGGER.info("[VesselDepartureMessageConsumer.listen()] - [End: {}]", MESSAGE_NAME);
            TimeUnit.SECONDS.sleep(5);
        } catch (IOException ioe) {
            LOGGER.error("[VesselDepartureMessageConsumer.listen()] - [IO Exception: {}]", ioe.getMessage());
            throw new IOException(ioe.getMessage());
        } catch (InterruptedException ine) {
            LOGGER.error("[VesselDepartureMessageConsumer.listen()] - [Interrupted Exception: {}]", ine.getMessage());
        }
    }

}
