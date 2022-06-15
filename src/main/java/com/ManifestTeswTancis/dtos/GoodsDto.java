package com.ManifestTeswTancis.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GoodsDto implements Serializable {
	 private String goodsItemNo;
     private String description;
     private String packingType;
     private Integer packageQuantity;
     private String packageType;
     private String oilType;
     private Double invoiceValue;
     private String invoiceCurrency;
     private Double insuranceValue;
     private String insuranceCurrency;
     private Double freightCharge;
     private String freightCurrency;
     private Double grossWeight;
     private String grossWeightUnit;
     private Double netWeight;
     private String netWeightUnit;
     private Double volume;
     private String volumeUnit;
     private Double length;
     private String lengthUnit;
     private Double width;
     private String widthUnit;
     private Double height;
     private String heightUnit;
     private String marksNumbers;
     private String vehicleVIN;
     private String vehicleModel;
     private String vehicleMake;
     private String vehicleOwnDrive;
     private DangerousGoodInformation dangerousGoodsInformation;
	 private List<GoodPlacementDto> placements = new ArrayList<>();

}
