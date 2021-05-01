package com.ManifestTeswTancis.Entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "TANCISINT.LIQUID_BK_QUALITY_REPORT")
public class LiquidBulkQualityReportEntity implements Serializable {
	private static final long serialVersionUID = -1865713825376141569L;

	@Id
	@Column(name = "TBS_CD_NO")
	private String tbsCodeNo;

	@Column(name = "MRN")
	private String mrn;

	@Column(name = "CALL_ID")
	private String call_id;

	@Column(name = "CLIENT")
	private String client;

	@Column(name = "CLIENT_ADDRESS")
	private String clientAddress;

	@Column(name = "SAMPLE")
	private String sample;

	@Column(name = "SITE_AND_POSITION_SAMPLED")
	private String siteAndPositionSampled;

	@Column(name = "SAMPLING_PLAN")
	private String samplingPlan;

	@Column(name = "DATE_OF_SAMPLING")
	private String dateOfSampling;

	@Column(name = "DATE_OF_RECEIVED")
	private String dateOfReceived;

	@Column(name = "DATE_STARTED")
	private String dateStarted;

	@Column(name = "DATE_FINISHED")
	private String dateFinished;

	@Column(name = "SPECIFICATION")
	private String specification;

	@Column(name = "TEST_METHOD")
	private String testMethod;

	@Column(name = "REPORT_URL")
	private String reportUrl;

	@Column(name = "REPORT_DATE")
	private String reportDate;

	@Column(name = "TESTMETHOD_DENSITYAT15")
	private String testMethodDensityAt15;

	@Column(name = "REQUIREMENT_DENSITYAT15")
	private String requirementDensityAt15;

	@Column(name = "RESULT_DENSITYAT15")
	private String resultDensityAt15;

	@Column(name = "PASSFAIL_DENSITYAT15")
	private String passFailAtDensity15;

	@Column(name = "TESTMETHOD_DENSITYAT20")
	private String testMethodDensityAt20;

	@Column(name = "REQUIREMENT_DENSITYAT20")
	private String requirementDensityAt20;

	@Column(name = "RESULT_DENSITYAT20")
	private String resultDensityAt20;

	@Column(name = "PASSFAIL_DENSITYAT20")
	private String passFailDensityAt20;

	@Column(name = "TESTMETHOD_COMMERCIALCOLOUR")
	private String testMethodCommercialColour;

	@Column(name = "REQUIREMENT_COMMERCIALCOLOUR")
	private String requirementCommercialColour;

	@Column(name = "RESULT_COMMERCIALCOLOUR")
	private String resultCommercialColour;

	@Column(name = "PASSFAIL_COMMERCIALCOLOUR")
	private String passFailCommercialColour;

	@Column(name = "TESTMETHOD_DOCTORTEST")
	private String testMethodDoctorTest;

	@Column(name = "REQUIREMENT_DOCTORTEST")
	private String requirementDoctorTest;

	@Column(name = "RESULT_DOCTORTEST")
	private String resultDoctorTest;

	@Column(name = "PASSFAIL_DOCTORTEST")
	private String passFailDoctorTest;

	@Column(name = "TESTMETHOD_APPEARANCE")
	private String testMethodAppearance;

	@Column(name = "REQUIREMENT_APPEARANCE")
	private String requirementAppearance;

	@Column(name = "RESULT_APPEARANCE")
	private String resultAppearance;

	@Column(name = "PASSFAIL_APPEARANCE")
	private String passFailAppearance;
}
