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
public class BillGeneralId implements Serializable {
    private String billCustomsOfficeCd;
    private String billYy;
    private String billTypeCd;
    private String billSerialNo;
    private Double billDg;
}
