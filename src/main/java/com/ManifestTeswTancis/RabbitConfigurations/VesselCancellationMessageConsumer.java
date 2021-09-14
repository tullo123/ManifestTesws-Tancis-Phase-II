package com.ManifestTeswTancis.RabbitConfigurations;

import com.ManifestTeswTancis.ServiceImpl.VesselCancellationServiceImpl;
import com.ManifestTeswTancis.dtos.CallInfCancelDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class VesselCancellationMessageConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(VesselCancellationMessageConsumer.class);
    private static final String MESSAGE_NAME = "VESSEL_CANCELLATION_NOTICE";
    private static final String QUEUE_NAME = "q.tancis.in.vessel-cancellation-notice";
    final ObjectMapper objectMapper;
    final VesselCancellationServiceImpl VesselCancellationServiceImpl;

    public VesselCancellationMessageConsumer(ObjectMapper objectMapper, VesselCancellationServiceImpl VesselCancellationServiceImpl) {
        this.objectMapper = objectMapper;
        this.VesselCancellationServiceImpl = VesselCancellationServiceImpl;
    }
    @RabbitListener(queues = QUEUE_NAME, containerFactory = "createListener")
    public void listen(String message) throws IOException {
        try {
            LOGGER.info("[VesselCancellationMessageConsumer.listen()] - [Start: {}]", MESSAGE_NAME);
            VesselCancellationMessageDto vesselCancellationMessageDto = objectMapper.readValue(message, VesselCancellationMessageDto.class);
            LOGGER.info("VesselCancellationMessageConsumer.listen()] - [VesselCancellationMessageConsumer: {}]", vesselCancellationMessageDto);
            CallInfCancelDto callInfCancelDto = vesselCancellationMessageDto.getPayload();
            System.out.println(callInfCancelDto);
            VesselCancellationServiceImpl.cancelVesselInfo(callInfCancelDto);
            LOGGER.info("[VesselCancellationMessageConsumer.listen()] - [End: {}]", MESSAGE_NAME);
            TimeUnit.SECONDS.sleep(5);
        } catch (IOException ioe) {
            LOGGER.error("[VesselCancellationMessageConsumer.listen()] - [IO Exception: {}]", ioe.getMessage());
            throw new IOException(ioe.getMessage());
        } catch (InterruptedException ine) {
            LOGGER.error("[VesselCancellationMessageConsumer.listen()] - [Interrupted Exception: {}]", ine.getMessage());
        }
    }
}
