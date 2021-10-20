package com.ManifestTeswTancis.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

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
     private LocalDateTime estimatedDatetimeOfDeparture;
     private LocalDateTime estimatedDatetimeOfArrival;
     private LocalDateTime actualDatetimeOfArrival;
     private LocalDateTime actualDatetimeOfArrivalOuterAnchorage;
     private LocalDateTime clearanceRequestDate;
     private String comments;
     private Double cnQuantityLoaded;
     private Double cnWeightLoaded;
     private Double bkQuantityLoaded;
     private Double bkWeightLoaded;
     private Double carQuantityLoaded;
     private Double carWeightLoaded;
}
