package com.ManifestTeswTancis.RabbitConfigurations;

import com.ManifestTeswTancis.ServiceImpl.ExportManifestServiceImpl;
import com.ManifestTeswTancis.dtos.ExportManifestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class ExportManifestMessageConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExportManifestMessageConsumer.class);
    private static final String MESSAGE_NAME = "SUBMITTED_EXPORT_MANIFEST";
    private static final String QUEUE_NAME = "q.tancis.in.submitted-export-manifest";
    final ObjectMapper objectMapper;
    final ExportManifestServiceImpl exportManifestServiceImpl;

    public ExportManifestMessageConsumer(ObjectMapper objectMapper, ExportManifestServiceImpl exportManifestServiceImpl) {
        this.objectMapper = objectMapper;
        this.exportManifestServiceImpl = exportManifestServiceImpl;
    }


    @RabbitListener(queues = QUEUE_NAME, containerFactory = "createListener")
    public void listen(String message) throws IOException {
        try {
            LOGGER.info("[ExportManifestMessageConsumer.listen()] - [Start: {}]", MESSAGE_NAME);
            ExportManifestMessageDto exportManifestMessageDto = objectMapper.readValue(message, ExportManifestMessageDto.class);
            LOGGER.info("ExportManifestMessageConsumer.listen()] - [ExportManifestMessageConsumer: {}]", exportManifestMessageDto);
            ExportManifestDto exportManifestDto = exportManifestMessageDto.getPayload();
            System.out.println(exportManifestDto);
            exportManifestServiceImpl.createExportManifest(exportManifestDto);
            LOGGER.info("[ExportManifestMessageConsumer.listen()] - [End: {}]", MESSAGE_NAME);
            TimeUnit.SECONDS.sleep(5);
        } catch (IOException ioe) {
            LOGGER.error("[ExportManifestMessageConsumer.listen()] - [IO Exception: {}]", ioe.getMessage());
            throw new IOException(ioe.getMessage());
        } catch (InterruptedException ine) {
            LOGGER.error("[ExportManifestMessageConsumer.listen()] - [Interrupted Exception: {}]", ine.getMessage());
        }
    }
}
