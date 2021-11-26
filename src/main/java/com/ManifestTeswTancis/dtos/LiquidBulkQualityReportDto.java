package com.ManifestTeswTancis.dtos;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LiquidBulkQualityReportDto implements Serializable {
	@NotNull(message="controlReferenceNumber should not be null")
	@NotEmpty(message = "controlReferenceNumber should not be empty")
	private String controlReferenceNumber;
	private String tbsCodeNo;
	@NotNull(message="mrn should not be null")
	@NotEmpty(message = "mrn should not be empty")
	private String mrn;
	@NotNull(message="communicationAgreedId should not be null")
	@NotEmpty(message = "communicationAgreedId should not be empty")
	private String communicationAgreedId;
	private String client;
	private String clientAddress;
	private String sample;
	private String siteAndPositionSampled;
	private String samplingPlan;
	private String dateOfSampling;
	private String dateOfReceived;
	private String dateStarted;
	private String dateFinished;
	private String specification;
	private String testMethod;
	private String reportUrl;
	private String reportDate;
	private String requirement;
	private String result;
	private String passFail;
	private TestResultDto testResult;

}
