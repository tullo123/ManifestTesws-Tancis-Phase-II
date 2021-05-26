package com.ManifestTeswTancis.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CallInfDto implements Serializable{
	private static final long serialVersionUID = -4404801375925781679L;
	private String mrn;
	private String communicationAgreedId;
	private String controlReferenceNumber;
	private String vesselMaster;
	private String vesselMasterAddress;
	private String agentCode;
	private String agentAddress;
	private String callSign;
	private String terminalOperatorCode;
	private String transportStageType;
	private String voyageNumber;
	private Integer modeOfTransportCoded;
	private String modeOfTransport;
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
	private LocalDateTime actualDatetimeOfDepartureOuterAnchorage;
	private LocalDateTime handoverDatetime;
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
	private Double carQuantityAtDischarge;
	private Double carQuantityAtDestination;
	private Double carWeightLoaded;
	private Double carWeightAtDischarge;
	private Double carWeightAtDestination;
	private String mrnDate = new SimpleDateFormat("MM/dd/yyyy").format(Calendar.getInstance().getTime());

}
