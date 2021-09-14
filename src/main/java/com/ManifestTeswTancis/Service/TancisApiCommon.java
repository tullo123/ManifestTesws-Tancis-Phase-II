package com.ManifestTeswTancis.Service;

import com.ManifestTeswTancis.RabbitConfigurations.MessageProducer;
import com.ManifestTeswTancis.dtos.ErrorDetailDto;
import com.ManifestTeswTancis.dtos.FeedbackDto;
import com.ManifestTeswTancis.dtos.FeedbackMessageDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Service
public class TancisApiCommon {
    final MessageProducer rabbitMqMessageProducer;
    private static final Logger LOGGER = LoggerFactory.getLogger(TancisApiCommon.class);
    public TancisApiCommon(MessageProducer rabbitMqMessageProducer) {
        this.rabbitMqMessageProducer = rabbitMqMessageProducer;
    }

    public void produceFeedbackMessage(String requestId, boolean errorYn, List<ErrorDetailDto> errorList, String outboundExchange, String callbackUrl, HttpStatus httpStatus) throws JsonProcessingException {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String preparationDateTime = LocalDateTime.now(ZoneId.of("Africa/Dar_es_Salaam")).format(dateTimeFormatter);
        FeedbackDto feedbackDto = new FeedbackDto(preparationDateTime, requestId, requestId, "APERAK", "9", requestId, errorYn, errorList);
        FeedbackMessageDto feedbackMessageDto = new FeedbackMessageDto(httpStatus, feedbackDto);
        rabbitMqMessageProducer.sendMessage(outboundExchange, "FEEDBACK", requestId, callbackUrl, feedbackMessageDto);
       LOGGER.info("[Common.produceFeedbackMessage()] - [Feedback message successfully added to queue] - [payload: {}]", feedbackMessageDto);

    }
}
