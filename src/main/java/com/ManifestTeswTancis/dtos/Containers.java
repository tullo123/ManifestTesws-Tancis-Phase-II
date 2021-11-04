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
public class Containers implements Serializable {
    private String action;
    private String containerNo;
    private String containerSize;
    private String typeOfContainer;
    private String freightIndicator;
    private String emptyFullStatus;
    private String length;
    private String lengthUnit;
    private String width;
    private String widthUnit;
    private String height;
    private String heightUnit;
    private Double volume;
    private String volumeUnit;
    private Double weight;
    private String weightUnit;
    private String grossWeight;
    private String grossWeightUnit;
    private String temperature;
    private String temperatureType;
    private String temperatureUnit;
    private Double minimumTemperature;
    private Double maximumTemperature;
    private List<SealNumberDto> sealNumbers = new ArrayList<>();
}
