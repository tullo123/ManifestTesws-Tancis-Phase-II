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
public class ExportChangeGeneralId implements Serializable {
    private String declarantTin;
    private String declarantYear;
    private String processType;
    private String declarantSerialNo;
}
