package com.ManifestTeswTancis.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExportContainersDto implements Serializable {
    private String containerNo;
    private String containerSize;
    private String typeOfContainer;
    private List<SealNumberDto>sealNumbers;
    private String freightIndicator;
    private String emptyFullStatus;
    private Double length;
    private String lengthUnit;
    private Double width;
    private String widthUnit;
    private Double height;
    private String heightUnit;
    private Double volume;
    private String volumeUnit;
    private Double weight;
    private String weightUnit;
    private Double grossWeight;
    private String grossWeightUnit;
    private Double temperature;
    private String temperatureType;
    private String temperatureUnit;
    private Double minimumTemperature;
    private Double maximumTemperature;
}
