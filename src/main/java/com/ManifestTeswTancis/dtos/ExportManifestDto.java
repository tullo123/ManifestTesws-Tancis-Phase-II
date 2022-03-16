package com.ManifestTeswTancis.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExportManifestDto implements Serializable {
    private String preparationDateTime;
    private String communicationAgreedId;
    private String controlReferenceNumber;
    private String applicationReference;
    private String manifestReferenceNumber;
    private String cargoReportDate;
    private String transportStageType;
    private String voyageNumber;
    private String carrierId;
    private String carrierName;
    private String transportMeansId;
    private String transportMeansName;
    private String transportMeansNationality;
    private String shippingLineCode;
    private String shippingLineName;
    private String shippingAgentCode;
    private String customOfficeCode;
    private String destinationPort;
    private String portOfCall;
    private String nextPortOfCall;
    private String terminal;
    private String terminalOperator;
    private ExportManifestSummaryDto manifestSummary;
    private String estimatedDatetimeOfDeparture;
    private String estimatedDatetimeOfArrival;
    private List<ExportBillOfLadingDto> consignments;
    private List<ContainerDto>containers;
}
