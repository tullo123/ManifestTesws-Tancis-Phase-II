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
public class LiquidBulkDischargeSequence implements Serializable {
	private static final long serialVersionUID = -5560619972028068859L;
	private String refNo;
	private String voyageNo;
	private String vesselName;
	private String imoNo;
	private String destinationPort;
	private String refdt;
	private Integer blQnt;
	private String product;
	private String terminal;
	private Integer quantity;
	private String controlReferenceNumber;
	private List<PumpingSequence> pumpingSequence;
}
