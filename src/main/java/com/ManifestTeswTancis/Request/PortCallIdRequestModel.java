package com.ManifestTeswTancis.Request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PortCallIdRequestModel {
	private String communicationAgreedId;
	private String controlReferenceNumber;
	private String vesselMaster;
	private String vesselMasterAddress;
	private String agentCode;
	private String messageTypeId;
	private String applicationReferenceNumber;
	private LocalDateTime processingDate;
	private LocalDateTime lastUpdateDate;
	private String agentAddress;
	private String terminalOperatorCode;
	private String voyageNumber;
	private String voyageNumberOutbound;
	private String modeOfTransport;
	private String callSign;
	private String carrierId;
	private String carrierName;
	private String transportMeansId;
	private String transportMeansName;
	private String transportMeansNationality;
	private String terminal;
	private String destinationPort;
	private String portOfCall;
	private String nextPortOfCall;
	private String customOfficeCode;
	private LocalDateTime estimatedDatetimeOfDeparture;
	private LocalDateTime actualDatetimeOfDeparture;
	private LocalDateTime estimatedDatetimeOfArrival;
	private LocalDateTime actualDatetimeOfArrival;
	private Double cnQuantityLoaded;
	private Double cnQuantityAtDischarge;
	private Double cnQuantityAtDestination;
	private Double cnWeightLoaded;
	private Double cnWeightAtDischarge;
	private Double cnWeightAtDestination;
	private Double bkQuantityLoaded;
	private Double bkQuantityAtDischarge;
	private Double bkQuantityAtDestination;
	private Double bkWeightLoaded;
	private Double bkWeightAtDischarge;
	private Double bkWeightAtDestination;
	private Double carQuantityLoaded;
	private Integer carQuantityAtDischarge;
	private Integer carQuantityAtDestination;
	private Double carWeightLoaded;
	private Double carWeightAtDischarge;
	private Double carWeightAtDestination;
	private String ballast;
	private boolean outwardCargo;

}
