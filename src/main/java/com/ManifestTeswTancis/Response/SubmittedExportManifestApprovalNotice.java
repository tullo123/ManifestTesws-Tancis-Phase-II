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
public class SubmittedExportManifestApprovalNotice implements Serializable {
    private String manifestReferenceNumber;
    private String communicationAgreedId;
    private String approvalStatus;
    private String approvalDate;
}
