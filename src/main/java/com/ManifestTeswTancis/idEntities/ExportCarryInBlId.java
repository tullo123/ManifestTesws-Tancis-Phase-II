package com.ManifestTeswTancis.idEntities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ExportCarryInBlId implements Serializable {
    private String expectedCarryInMasterBlNo;
    private String expectedCarryInHouseBlNo;
    private String carryInType;
    private String expectedCarryInBaseNo;
    private String warehouseCode;
}
