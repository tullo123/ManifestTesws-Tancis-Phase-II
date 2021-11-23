package com.ManifestTeswTancis.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FreePratiqueReportDto implements Serializable {
    private String preparationDateTime;
    private String communicationAgreedId;
    private String vesselMaster;
    private String vesselMasterAddress;
    private String agentCode;
    private String agentAddress;
    private String voyageNumber;
    private String callSign;
    private String transportMeansId;
    private String transportMeansName;
    private String transportMeansNationality;
    private String inspectionVerdict;
    private LocalDateTime inspectionDate;
    private String reportLink;
}
