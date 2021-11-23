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
public class VesselDocumentationDto implements Serializable {
     private String preparationDateTime;
     private String communicationAgreedId;
     private String controlReferenceNumber;
     private String vesselMaster;
     private String vesselMasterAddress;
     private String agentCode;
     private String agentAddress;
     private String terminalOperatorCode;
     private String voyageNumber;
     private String carrierId;
     private String carrierName;
     private String callSign;
     private String transportMeansId;
     private String transportMeansName;
     private String transportMeansNationality;
     private String terminal;
     private String destinationPort;
     private String portOfCall;
     private String nextPortOfCall;
      List<DocumentDto> documents;
}
