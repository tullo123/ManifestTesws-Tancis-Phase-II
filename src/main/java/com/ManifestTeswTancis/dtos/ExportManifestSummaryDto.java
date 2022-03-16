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
public class ExportManifestSummaryDto implements Serializable {
    private Integer numberOfMasterBl;
    private Integer numberOfHouseBl;
    private Integer numberOfContainers;
    private Integer numberOfVehicles;

}
