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
public class ExportChangeContainerId implements Serializable {
    private String declarantTin;
    private String declarantYear;
    private String processType;
    private String declarantSerialNo;
    private String warehouseCode;
    private String declarationNo;
    private String masterBillOfLading;
    private String houseBillOfLading;
    private String containerNo;
}
