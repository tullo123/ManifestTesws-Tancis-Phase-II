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
	private String preparationDateTime;
	private String communicationAgreedId;
	private String controlReferenceNumber;
	private String messageTypeId;
	private String messageFunction;
	private String vesselMaster;
	private String vesselMasterAddress;
	private String agentCode;
	private String agentAddress;
	private String voyageNumber;
	private String modeOfTransportCoded;
	private String modeOfTransport;
	private String carrierId;
	private String transportMeansId;
	private String transportMeansName;
	private String transportMeansNationality;
	private String rotationNumber;
	private String nextPortOfCall;
	private String actualDatetimeOfDeparture;
	private String actualDatetimeOfDepartureOuterAnchorage;
	private String handoverDatetime;

}
