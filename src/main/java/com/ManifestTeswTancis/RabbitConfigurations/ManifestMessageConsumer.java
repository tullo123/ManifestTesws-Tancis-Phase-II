package com.ManifestTeswTancis.RabbitConfigurations;

import com.ManifestTeswTancis.ServiceImpl.ExImportManifestServiceImp;
import com.ManifestTeswTancis.dtos.ManifestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class ManifestMessageConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ManifestMessageConsumer.class);
    private static final String MESSAGE_NAME = "SUBMITTED_MANIFEST";
    private static final String QUEUE_NAME = "q.tancis.in.submitted-manifest";
    final ObjectMapper objectMapper;
    final ExImportManifestServiceImp exImportManifestServiceImp;

    public ManifestMessageConsumer(ObjectMapper objectMapper, ExImportManifestServiceImp exImportManifestServiceImp) {
        this.objectMapper = objectMapper;
        this.exImportManifestServiceImp = exImportManifestServiceImp;
    }
    @RabbitListener(queues = QUEUE_NAME, containerFactory = "createListener")
    public void listen(String message) throws IOException {
        try {
            LOGGER.info("[ManifestMessageConsumer.listen()] - [Start: {}]", MESSAGE_NAME);
            ManifestMessageDto manifestMessageDto = objectMapper.readValue(message, ManifestMessageDto.class);
            LOGGER.info("ManifestMessageConsumer.listen()] - [ManifestMessageConsumer: {}]", manifestMessageDto);
            ManifestDto manifestDto = manifestMessageDto.getPayload();
            System.out.println(manifestDto);
            exImportManifestServiceImp.createManifest(manifestDto);
            LOGGER.info("[ManifestMessageConsumer.listen()] - [End: {}]", MESSAGE_NAME);
            TimeUnit.SECONDS.sleep(5);
        } catch (IOException ioe) {
            LOGGER.error("[ManifestMessageConsumer.listen()] - [IO Exception: {}]", ioe.getMessage());
            throw new IOException(ioe.getMessage());
        } catch (InterruptedException ine) {
            LOGGER.error("[ManifestMessageConsumer.listen()] - [Interrupted Exception: {}]", ine.getMessage());
        }
    }
}
