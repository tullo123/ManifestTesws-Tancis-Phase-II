package com.ManifestTeswTancis.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ContainerDtoAmend {
    private String declarantTin;
    private Integer amendYear;
    private String processType;
    private String amendSerialNumber;
    private String billOfLading;
    private String containerNo;
    private String containerSize;
    private String typeOfContainer;
    private String sealNumberOne;
    private String sealNumberTwo;
    private String sealNumberThree;
    private String customSealNumberOne;
    private String customSealNumberTwo;
    private String customSealNumberThree;
    private String freightIndicator;
    private Double volume;
    private String volumeUnit;
    private Double weight;
    private String weightUnit;
    private String referPlugYn;
    private Double minimumTemperature;
    private Double maximumTemperature;
    private String imdgCd;
    private String firstRegisterId;
    private Date lastUpdateId;
    private Date lastUpdateDate;
    private String temperatureType;
    private String emptyFullStatus;
    private Double length;
    private String lengthUnit;
    private Double width;
    private String widthUnit;
    private Double height;
    private String heightUnit;
    private Double grossWeight;
    private String grossWeightUnit;
    List<SealNumberDto> sealNumbers;

}
