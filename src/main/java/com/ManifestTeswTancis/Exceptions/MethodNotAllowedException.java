package com.ManifestTeswTancis.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED)
public class MethodNotAllowedException extends Exception{
    public MethodNotAllowedException(String message) {
        super(message);
    }
}
