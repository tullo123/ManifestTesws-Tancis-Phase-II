package com.ManifestTeswTancis.RabbitConfigurations;

import com.ManifestTeswTancis.ServiceImpl.ExImportManifestAmendServiceImpl;
import com.ManifestTeswTancis.dtos.ManifestAmendmentDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class ManifestAmendmentMessageConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ManifestAmendmentMessageConsumer.class);
    private static final String MESSAGE_NAME = "MANIFEST_AMENDMENT";
    private static final String QUEUE_NAME = "q.tancis.in.manifest-amendment";
    final ObjectMapper objectMapper;
    final ExImportManifestAmendServiceImpl exImportManifestAmendServiceImpl;

    public ManifestAmendmentMessageConsumer(ObjectMapper objectMapper, ExImportManifestAmendServiceImpl exImportManifestAmendServiceImpl) {
        this.objectMapper = objectMapper;
        this.exImportManifestAmendServiceImpl = exImportManifestAmendServiceImpl;
    }
    @RabbitListener(queues = QUEUE_NAME, containerFactory = "createListener")
    public void listen(String message) throws IOException {
        try {
            LOGGER.info("[ManifestAmendmentMessageConsumer.listen()] - [Start: {}]", MESSAGE_NAME);
            ManifestAmendmentMessageDto manifestAmendmentMessageDto = objectMapper.readValue(message, ManifestAmendmentMessageDto.class);
            LOGGER.info("ManifestAmendmentMessageConsumer.listen()] - [ManifestAmendmentMessageConsumer: {}]", manifestAmendmentMessageDto);
            ManifestAmendmentDto manifestAmendmentDto = manifestAmendmentMessageDto.getPayload();
            System.out.println(manifestAmendmentDto);
            exImportManifestAmendServiceImpl.amendManifest(manifestAmendmentDto);
            LOGGER.info("[ManifestAmendmentMessageConsumer.listen()] - [End: {}]", MESSAGE_NAME);
            TimeUnit.SECONDS.sleep(5);
        } catch (IOException ioe) {
            LOGGER.error("[ManifestAmendmentMessageConsumer.listen()] - [IO Exception: {}]", ioe.getMessage());
            throw new IOException(ioe.getMessage());
        } catch (InterruptedException ine) {
            LOGGER.error("[ManifestAmendmentMessageConsumer.listen()] - [Interrupted Exception: {}]", ine.getMessage());
        }
    }
}
