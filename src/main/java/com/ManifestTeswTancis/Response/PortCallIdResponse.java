package com.ManifestTeswTancis.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PortCallIdResponse {
	private String mrnIn;
	private String mrnOut;
	private String mrnDate;
	private String customOfficeCode;
	private String communicationAgreedId;
}
