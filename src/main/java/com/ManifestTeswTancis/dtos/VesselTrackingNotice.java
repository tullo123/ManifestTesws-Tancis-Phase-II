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
public class VesselTrackingNotice {
    private String preparationDateTime;
    private String communicationAgreedId;
    private String controlReferenceNumber;
    private String messageTypeId;
    private String messageFunction;
    private String vesselMaster;
    private String vesselMasterAddress;
    private String agentCode;
    private String agentAddress;
    private String terminalOperatorCode;
    private String transportStageType;
    private String voyageNumber;
    private String modeOfTransportCoded;
    private String modeOfTransport;
    private String carrierId;
    private String carrierName;
    private String transportMeansId;
    private String transportMeansName;
    private String transportMeansNationality;
    private String voyageNumberOutbound;
    private String terminal;
    private String rotationNumber;
    private String berthNo;
    private boolean ballast;
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
    private LocalDateTime actualDatetimeOfArrivalOuterAnchorage;
    private Double draftFore;
    private String draftForeUnit;
    private Double draftAfter;
    private String draftAfterUnit;
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
}
