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
public class ExportChangeApplicationDto implements Serializable {
     private String messageId;
     private String declarantTin;
     private String warehouseCode;
     private String masterBlNo;
     private String houseBlNo;
     private String customsOfficeCode;
     private String changeType;
     private String changeReason;
     private Integer changePackage;
     private String packageUnit;
     private Double changeGrossWeight;
     private Double changeNetWeight;
     private String netWeightUnit;
     private String registeredDate;
     private String registeredBy;
     private String updatedDate;
     private String updatedBy;
     private String submitDate;
     private String submittedBy;
    private List<ExportChangeApplicationCargoDto> Cargo;
}
