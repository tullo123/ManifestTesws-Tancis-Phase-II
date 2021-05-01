package com.ManifestTeswTancis.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SealNumberDto {
	private String sealNumber;
	private String sealNumberIssuerType;
	private String  sealNumberIssuerName;
	private String sealNumberCondition;
}
