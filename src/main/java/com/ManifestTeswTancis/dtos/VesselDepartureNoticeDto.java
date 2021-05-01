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
public class VesselDepartureNoticeDto implements Serializable {
	private static final long serialVersionUID = 8053510787316899781L;

	private String communicationAgreedId;


	private String vesselMaster;


	private String vesselMasterAddress;

	private String agentCode;


	private String agentAddress;

	private String voyageNumber;

	private String modeOfTransport;

	private String carrierId;

	private String carrierName;

	private String transportMeansId;

	private String transportMeansName;

	private String transportMeansNationality;

	private String voyageNumberOutbound;

	private String terminalOperatorCode;

	private String terminal;

	private String destinationPort;

	private String portOfCall;

	private String nextPortOfCall;

	private String estimatedDatetimeOfArrival;

	private String estimatedDatetimeOfDeparture;

	private String actualDatetimeOfArrivalOuterAnchorage;

	private String handoverDatetime;

	private String actualDatetimeOfArrival;

	private String actualDatetimeOfDeparture;

	private String actualDatetimeOfDepartureOuterAnchorage;

	private String controlReferenceNumber;

	private String messageTypeId;

	private String messageFunction;

	private String transportStageType;

	private String modeOfTransportCoded;

	private String rotationNumber;

	private String berthNo;

	private Integer quantity;

	private String quantityUnit;

	private Integer quantityOutbound;

	private String quantityUnitOutbound;

	private Integer draftFore;

	private String draftForeUnit;

	private Integer draftAfter;

	private String draftAfterUnit;
	

}
