package com.ManifestTeswTancis.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ManifestApprovalResponseStatus {
	private String messageReferenceNumber;
	private String communicationAgreedId;
	private String mrn;
	private String approvalStatus;
	private String approvalDate;
	List<ResponseBl> bls;
}
