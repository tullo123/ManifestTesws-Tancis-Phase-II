package com.ManifestTeswTancis.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VesselBoardingNotificationDto {
	private String senderId;
    private String receiverId;
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
    private String customOfficeCode;
    private String mrn;
    private String clearanceRequestDate;

}
