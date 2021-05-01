package com.ManifestTeswTancis.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CallInfCancelDto {
    private String communicationAgreedId;
    private String mrn;
    private String customOfficeCode;
    private String controlReferenceNumber;
}
