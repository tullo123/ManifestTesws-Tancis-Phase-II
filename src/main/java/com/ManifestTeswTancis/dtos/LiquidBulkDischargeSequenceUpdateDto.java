package com.ManifestTeswTancis.dtos;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LiquidBulkDischargeSequenceUpdateDto implements Serializable {
	private static final long serialVersionUID = 8813889591984640144L;
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
	private List<PumpingSequenceDto> pumpingSequence;
}
