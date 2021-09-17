package com.ManifestTeswTancis.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomClearanceApprovalResponse {
    private String communicationAgreedId;
    private String clearanceReference;
    private String approvalStatus;
    private String comment;
    private String noticeDate;
}
