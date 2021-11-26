package com.ManifestTeswTancis.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PumpingSequenceFinalDischargeDto implements Serializable {
        private String terminal;
        private Double quantity;
        private LocalDateTime startDateTime;
        private LocalDateTime endDateTime;
        private String status;
}
