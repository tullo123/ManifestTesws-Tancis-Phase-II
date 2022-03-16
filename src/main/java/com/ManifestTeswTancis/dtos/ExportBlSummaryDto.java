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
public class ExportBlSummaryDto implements Serializable {
    private Integer numberOfVehicles;
    private Integer numberOfHouseBl;
    private Integer numberOfContainers;
    private Double blGrossWeight;
    private Double blNetWeight;
}
