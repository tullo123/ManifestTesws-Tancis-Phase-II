package com.ManifestTeswTancis.dtos;

import java.io.Serializable;
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
	private String call_id;
	private String vesselName;
	private String imoNo;
	private String destinationPort;
	private String refdt;
	private Integer blQnt;
	private String product;
	private String controlReferenceNumber;
	private List<PumpingSequenceDto> pumpingSequence;

}
