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
public class InAmendGenId implements Serializable {
    private String declarantTin;
    private String amendYear;
    private String processType;
    private String amendSerialNumber;
}
