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
public class AmendPenaltyId implements Serializable {
	private static final long serialVersionUID = 4253973932559396163L;
	private String declarantTin;
	private String amendYear;
	private String processType;
	private String amendSerialNumber;
	private String penaltyType;
	private String invoiceNumber;
	private String accountNumber;
}
