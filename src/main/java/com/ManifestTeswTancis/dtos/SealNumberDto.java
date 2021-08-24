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
public class SealNumberDto implements Serializable {
	private String sealNumber;
	private String sealNumberIssuerType;
	private String sealNumberIssuerName;
	private String sealNumberCondition;
}
