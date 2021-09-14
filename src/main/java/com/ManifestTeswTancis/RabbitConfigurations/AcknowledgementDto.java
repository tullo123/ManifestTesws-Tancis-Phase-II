package com.ManifestTeswTancis.RabbitConfigurations;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AcknowledgementDto implements Serializable {
    private String ackType;
    private String refId;
    private String code;
    private String description;
    private String ackDate;
}
