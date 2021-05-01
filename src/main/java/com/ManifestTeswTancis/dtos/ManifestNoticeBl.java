package com.ManifestTeswTancis.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ManifestNoticeBl {
	private String tasacControlNumber;
	private String msn;
	private String crn;
	private String hsn;
	private String masterBillOfLading;
	private String houseBillOfLading;
}
