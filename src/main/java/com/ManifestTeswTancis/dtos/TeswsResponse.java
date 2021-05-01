package com.ManifestTeswTancis.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TeswsResponse {
	private String ackType;
	private String refId;
	private Integer code = 200;
	private String description = "Received Successfully";
	private String ackDate;
}
