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
public class ManifestAmendmentPaymentNotice implements Serializable {
    private String msgRefNumb;
    private String paymentDate;
    private String billId;
    private String controlNumber;
    private List<Payment> payment = new ArrayList<>();
    private String payerName;
    private String payerPhoneNumber;
}
