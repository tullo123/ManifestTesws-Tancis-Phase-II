package com.ManifestTeswTancis.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ContainerDto {
	private String containerNo;
	private String masterBillOfLading;
	private String houseBillOfLading;
	private String msn;
	private String hsn;
	private String containerSize;
	private String typeOfContainer;
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
	private String amendSerialNumber;
	List<SealNumberDto> sealNumbers;
	//private SealNumberDto[] sealNumbers;

}
