package com.ManifestTeswTancis.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomClearanceDto {
     private String communicationAgreedId;
     private String vesselMaster;
     private String vesselMasterAddress;
     private String agentCode;
     private String agentAddress;
     private String voyageNumber;
     private String mrn;
     private String carrierId;
     private String carrierName;
     private String transportMeansName;
     private String transportMeansNationality;
     private String voyageNumberOutbound;
     private String terminal;
     private String destinationPort;
     private String portOfCall;
     private String nextPortOfCall;
     private String taxClearanceNumber;
     private String estimatedDatetimeOfDeparture;
     private String estimatedDatetimeOfArrival;
     private String actualDatetimeOfArrival;
     private String actualDatetimeOfArrivalOuterAnchorage;
     private String clearanceRequestDate;
     private String comments;
}
