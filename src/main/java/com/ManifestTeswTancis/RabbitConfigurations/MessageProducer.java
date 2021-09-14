package com.ManifestTeswTancis.RabbitConfigurations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
public class MessageProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageProducer.class);
    private static final String HEADER_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    private static final DateTimeFormatter HEADER_DATE_FORMATTER = DateTimeFormatter.ofPattern(HEADER_DATE_FORMAT);
    private final RabbitMqConfig rabbitMqConfig;

    public MessageProducer(RabbitMqConfig rabbitMqConfig) {
        this.rabbitMqConfig = rabbitMqConfig;
    }

    public AcknowledgementDto sendMessage(String exchangeName, String messageName, String requestId, String callbackUrl, Object object) {
        String TZ_TIME_ZONE_ID = "Africa/Dar_es_Salaam";
        LocalDateTime currentDateTime = LocalDateTime.now(ZoneId.of(TZ_TIME_ZONE_ID));
        String ackDateTime = currentDateTime.format(HEADER_DATE_FORMATTER);
        LOGGER.info("[MessageProducer.sendMessage()] - [1. Start: {}]", messageName);
        MessageDto messageDto = new MessageDto(messageName, requestId, callbackUrl, object);
        LOGGER.info("[MessageProducer.sendMessage()] - [2. messageDto: {}]", messageDto);
        //Get RabbitTemplate, convert and send the object to a queue as a json string
        rabbitMqConfig.getRabbitTemplate().convertAndSend(exchangeName, messageName, messageDto);
        LOGGER.info("[MessageProducer.sendMessage()] - [3. End: Published to RabbitMQ]");
        return new AcknowledgementDto(messageName, requestId, Integer.toString(HttpStatus.ACCEPTED.value()), HttpStatus.ACCEPTED.name(), ackDateTime);
    }
}
