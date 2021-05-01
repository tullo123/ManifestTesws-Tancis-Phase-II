package com.ManifestTeswTancis.dtos;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LiquidBulkQualityReportDto implements Serializable {
	private static final long serialVersionUID = 7244342455199470988L;
	private String controlReferenceNumber;
	private String tbsCodeNo;
	private String mrn;
	private String call_id;
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
	private TestResult testResult = new TestResult();

}
