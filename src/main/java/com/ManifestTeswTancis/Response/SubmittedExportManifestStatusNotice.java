package com.ManifestTeswTancis.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SubmittedExportManifestStatusNotice implements Serializable {
    private String noticeDate;
    private String communicationAgreedId;
    private String manifestReferenceNumber;
    private String voyageNumber;
    private String customOfficeCode;
    private String status;

}
