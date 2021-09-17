package com.ManifestTeswTancis.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SubmittedManifestStatusResponse {
    private String noticeDate;
    private String communicationAgreedId;
    private String controlReferenceNumber;
    private String applicationReference;
    private String voyageNumber;
    private String customOfficeCode;
    private String status;
}
