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
public class ExportChangeApplicationNotice implements Serializable {
    private String messageId;
    private String declarationNo;
    private String approvalStatus;
    private String approvalDate;
    private String approvalComment;
}
