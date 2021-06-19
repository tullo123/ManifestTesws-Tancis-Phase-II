package com.ManifestTeswTancis.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LiquidBulkFinalDischargeSequence implements Serializable {
    private String refNo;
    private String voyageNo;
    private String mrn;
    private String communicationAgreedId;
    private String vesselName;
    private String imoNo;
    private String callSign;
    private String destinationPort;
    private LocalDateTime refDate;
    private Double blQnt;
    private String oilType;
    private List<PumpingSequenceFinalDischarge>pumpingSequenceFinal;
}
