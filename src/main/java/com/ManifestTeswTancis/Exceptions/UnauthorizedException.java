package com.ManifestTeswTancis.Exceptions;

import com.ManifestTeswTancis.dtos.ErrorDetailDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@ToString
@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends Exception {
    public UnauthorizedException(List<ErrorDetailDto> errors) {
        this.errorMessages = new ArrayList<>();
        this.errorMessages = errors;
    }

    private List<ErrorDetailDto> errorMessages;
}
