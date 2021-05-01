package com.ManifestTeswTancis.Entity.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CallInfRest {
	private String mrn;
	private String mrnDate;
	private String customOfficeCode;
	private String communicationAgreedId;
}
