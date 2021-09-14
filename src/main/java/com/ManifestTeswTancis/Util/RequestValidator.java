package com.ManifestTeswTancis.Util;

import com.ManifestTeswTancis.dtos.ErrorDetailDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RequestValidator implements Serializable {
    boolean isValid;
    List<ErrorDetailDto> errors;
}
