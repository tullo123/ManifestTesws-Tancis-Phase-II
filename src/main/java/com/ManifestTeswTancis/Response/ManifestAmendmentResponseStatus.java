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
public class ManifestAmendmentResponseStatus implements Serializable {
        private String communicationAgreedId;
        private String noticeDate;
        private String amendmentReference;
        private String mrn;
        private String voyageNumber;
        private String approvalStatus;
        private String msn;
        private String hsn;
        private String crn;
        private String comment;
}
