package com.ManifestTeswTancis.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlMeasurement {
    private Integer pkQuantity;
    private String pkType;
    private String description;
    private Double grossWeight;
    private Double netWeight;
    private String weightUnit;
    private Double volume;
    private String volumeUnit;
    private String oilType;
    private String packingType;
    private String imdgClass;
    private String invoiceCurrency;
    private String freightCurrency;
    private Double invoiceValue;
    private Double freightCharge;
    private String marksNumbers;
}
