package com.ManifestTeswTancis.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ManifestAmendmentBillNotice implements Serializable {
    private String msgRefNumb;
    private String billReference;
    private String billReferenceType;
    private String generatedDate;
    private String payerInstitutionalCode;
    private String payeeInstitutionalCode;
    private String billId;
    private Double billAmount;
    private String ccy;
    private String controlNumber;
    private String generatedBy;
    private String approvedBy;
    private String approvedDate;
    private String description;
    private List<BillItem> billItems = new ArrayList<>();
}
