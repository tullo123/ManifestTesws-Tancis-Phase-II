package com.ManifestTeswTancis.dtos;

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
public class GoodDetails implements Serializable {
    private String goodsItemNo;
    private String description;
    private String packingType;
    private Integer packageQuantity;
    private String packageType;
    private String oilType;
    private Double invoiceValue;
    private String invoiceCurrency;
    private String insuranceValue;
    private String insuranceCurrency;
    private Double freightCharge;
    private String freightCurrency;
    private Double grossWeight;
    private String grossWeightUnit;
    private Double netWeight;
    private String netWeightUnit;
    private Double volume;
    private String volumeUnit;
    private String length;
    private String lengthUnit;
    private String width;
    private String widthUnit;
    private String height;
    private String heightUnit;
    private String marksNumbers;
    private String vehicleVIN;
    private String vehicleModel;
    private String vehicleMake;
    private String vehicleOwnDrive;
    private DangerousGoodInformation dangerousGoodsInformation;
    private List<Placements> placements = new ArrayList<>();
}
