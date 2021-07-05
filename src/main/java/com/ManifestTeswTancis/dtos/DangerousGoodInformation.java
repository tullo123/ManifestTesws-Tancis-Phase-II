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
public class DangerousGoodInformation implements Serializable {
    private String classCode;
    private String description;
    private String flashpointValue;
    private Double shipmFlashptValue;
    private String shipmFlashptUnit;
    private String packingGroup;
    private String marPolCategory;
    private String unnumber;
    private String imdgpage;
    private String imdgclass;
    private String ems;
    private String mfag;
    private String tremcard;
}
