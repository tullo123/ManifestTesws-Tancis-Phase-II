package com.ManifestTeswTancis.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExportChangeApplicationCargoDto implements Serializable {
    private String refDeclarantNo;
    private String msn;
    private String masterBlNo;
    private String containerNo;
    private String containerSize;
    private String packingType;
    private Integer numberOfPackages;
    private Double weight;
    private String vehicleVIN;
    private String vehicleModel;
    private String vehicleMake;
}
