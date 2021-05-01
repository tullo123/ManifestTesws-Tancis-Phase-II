package com.ManifestTeswTancis.Entity.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ManifestResponseStatus {
	private String messageReferenceNumber;
	private String callId;
	private String mrn;
	private String approvalStatus;
	private String approvalDt;
	List<ResponseBl> bls;
}
