package com.ManifestTeswTancis.Exceptions;

import com.ManifestTeswTancis.RabbitConfigurations.MessageProducer;
import com.ManifestTeswTancis.Util.WebUtils;
import com.ManifestTeswTancis.dtos.ErrorDetailDto;
import com.ManifestTeswTancis.dtos.FeedbackRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.MethodNotAllowedException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class AppExceptionsHandler {
    final MessageProducer messageProducer;
    final ObjectMapper objectMapper;
    final WebUtils webUtils;

    @Value("${spring.rabbitmq.exchange.out}")
    private String OUTBOUND_EXCHANGE;
    private static final Logger LOGGER = LoggerFactory.getLogger(AppExceptionsHandler.class);
    private static final String MESSAGE_NAME = "FEEDBACK";

    public AppExceptionsHandler(MessageProducer messageProducer, ObjectMapper objectMapper, WebUtils webUtils) {
        this.messageProducer = messageProducer;
        this.objectMapper = objectMapper;
        this.webUtils = webUtils;
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    public ResponseEntity<?> unauthorizedException(UnauthorizedException ex, WebRequest request) {
        List<ErrorDetailDto> errorList = ex.getErrorMessages();
        List<ErrorDetailDto> errors = new ArrayList<>(errorList);
        LOGGER.error("[{}] - [AppExceptionsHandler.unauthorizedException()] - [Exception: {}]", webUtils.getClientIp(), ex.toString());
        FeedbackRequestDto feedbackRequestDto = produceFeedbackMessage(request, errors);
        return new ResponseEntity<>(feedbackRequestDto, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = MethodNotAllowedException.class)
    public ResponseEntity<?> methodNotAllowedException(MethodNotAllowedException ex, WebRequest request) {
        List<ErrorDetailDto> errors = new ArrayList<>();
        ErrorDetailDto errorDetailDto = new ErrorDetailDto();
        errorDetailDto.setErrorCode(HttpStatus.METHOD_NOT_ALLOWED.toString());
        errorDetailDto.setErrorDescription(ex.getMessage());
        errors.add(errorDetailDto);
        FeedbackRequestDto feedbackRequestDto = produceFeedbackMessage(request, errors);
        return new ResponseEntity<>(feedbackRequestDto, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<?> methodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        List<ErrorDetailDto> errors = new ArrayList<>();
        for (FieldError x : ex.getBindingResult().getFieldErrors()) {
            ErrorDetailDto errorDetailDto = new ErrorDetailDto();
            errorDetailDto.setErrorCode(HttpStatus.BAD_REQUEST.toString());
            errorDetailDto.setErrorDescription(x.getDefaultMessage());
            errors.add(errorDetailDto);
        }
        FeedbackRequestDto feedbackRequestDto = produceFeedbackMessage(request, errors);
        return new ResponseEntity<>(feedbackRequestDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> generalException(Exception ex, WebRequest request) {
        List<ErrorDetailDto> errors = new ArrayList<>();
        ErrorDetailDto errorDetailDto = new ErrorDetailDto();
        errorDetailDto.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
        errorDetailDto.setErrorDescription(ex.getMessage());
        errors.add(errorDetailDto);
        FeedbackRequestDto feedbackRequestDto = produceFeedbackMessage(request, errors);
        return new ResponseEntity<>(feedbackRequestDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private FeedbackRequestDto produceFeedbackMessage(WebRequest request, List<ErrorDetailDto> errors) {
        FeedbackRequestDto feedbackRequestDto = new FeedbackRequestDto();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String preparationDateTime = LocalDateTime.now(ZoneId.of("Africa/Dar_es_Salaam")).format(dateTimeFormatter);
        feedbackRequestDto.setPreparationDateTime(preparationDateTime);
        feedbackRequestDto.setError(true);
        feedbackRequestDto.setMessageFunction("9");
        feedbackRequestDto.setMessageTypeId("APERAK");
        feedbackRequestDto.setControlReferenceNumber(request.getHeader("x-tesws-request-id"));
        feedbackRequestDto.setMessageReferenceNumber(request.getHeader("x-tesws-request-id"));
        feedbackRequestDto.setReceiverMessageReferenceNumber(request.getHeader("x-tesws-request-id"));
        feedbackRequestDto.setResponses(errors);
        String requestId = request.getHeader("x-tesws-request-id");
        String callbackUrl = request.getHeader("x-tesws-callback-url");
        messageProducer.sendMessage(OUTBOUND_EXCHANGE, MESSAGE_NAME, requestId, callbackUrl, feedbackRequestDto);
        LOGGER.info("[{}] - [AppExceptionsHandler.produceFeedbackMessage()] - [Successfully produced feedback message] - [payload: {}]", webUtils.getClientIp(), feedbackRequestDto);
        return feedbackRequestDto;
    }
}
