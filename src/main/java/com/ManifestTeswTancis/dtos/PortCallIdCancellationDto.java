package com.ManifestTeswTancis.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PortCallIdCancellationDto {
     private String cancelRef;
     private String communicationAgreedId;
     private String voyageNumber;
     private String transportMeansId;
     private String transportMeansName;
     private String transportMeansNationality;
     private String approvalStatus;
     private String approvalComment;
     private String noticeDt;
}
