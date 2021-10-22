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
public class ManifestAmendStagingTblId implements Serializable {
    private String mrn;
    private String communicationAgreedId;
    private String amendReference;
}
