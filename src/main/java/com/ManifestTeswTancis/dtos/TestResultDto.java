package com.ManifestTeswTancis.dtos;

import java.io.Serializable;

public class TestResultDto implements Serializable {
	private static final long serialVersionUID = -7257874289243411657L;
/*	private String densityAt15;
	private String densityAt20;
	private String commercialColour;
	private String doctorTest;
	private String appearance;
	*/
	private String testMethod;
    public TestsDto getDensityAt15() {
		return densityAt15;
	}
	public void setDensityAt15(TestsDto densityAt15) {
		this.densityAt15 = densityAt15;
	}
	public TestsDto getDensityAt20() {
		return densityAt20;
	}
	public void setDensityAt20(TestsDto densityAt20) {
		this.densityAt20 = densityAt20;
	}
	public TestsDto getCommercialColour() {
		return commercialColour;
	}
	public void setCommercialColour(TestsDto commercialColour) {
		this.commercialColour = commercialColour;
	}
	public TestsDto getDoctorTest() {
		return doctorTest;
	}
	public void setDoctorTest(TestsDto doctorTest) {
		this.doctorTest = doctorTest;
	}
	public TestsDto getAppearance() {
		return appearance;
	}
	public void setAppearance(TestsDto appearance) {
		this.appearance = appearance;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private String requirement;
    private String result;
    private String passFail;
    
    private TestsDto densityAt15;
    private TestsDto densityAt20;
    private TestsDto commercialColour;
    private TestsDto doctorTest;
    private TestsDto appearance;
    
	public TestResultDto() {
		densityAt15 = new TestsDto();
	    densityAt20 = new TestsDto();
	    commercialColour = new TestsDto();
	    doctorTest = new TestsDto();
	    appearance = new TestsDto();
	}
/*
	public TestResult(String densityAt15, String densityAt20, String commercialColour, String doctorTest,
			String appearance, String testMethod, String requirement, String result, String passFail) {
		this.densityAt15 = densityAt15;
		this.densityAt20 = densityAt20;
		this.commercialColour = commercialColour;
		this.doctorTest = doctorTest;
		this.appearance = appearance;
		this.testMethod = testMethod;
		this.requirement = requirement;
		this.result = result;
		this.passFail = passFail;
	}

	public String getDensityAt15() {
		return densityAt15;
	}

	public void setDensityAt15(String densityAt15) {
		this.densityAt15 = densityAt15;
	}

	public String getDensityAt20() {
		return densityAt20;
	}

	public void setDensityAt20(String densityAt20) {
		this.densityAt20 = densityAt20;
	}

	public String getCommercialColour() {
		return commercialColour;
	}

	public void setCommercialColour(String commercialColour) {
		this.commercialColour = commercialColour;
	}

	public String getDoctorTest() {
		return doctorTest;
	}

	public void setDoctorTest(String doctorTest) {
		this.doctorTest = doctorTest;
	}

	public String getAppearance() {
		return appearance;
	}

	public void setAppearance(String appearance) {
		this.appearance = appearance;
	}
*/
	public String getTestMethod() {
		return testMethod;
	}

	public void setTestMethod(String testMethod) {
		this.testMethod = testMethod;
	}

	public String getRequirement() {
		return requirement;
	}

	public void setRequirement(String requirement) {
		this.requirement = requirement;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getPassFail() {
		return passFail;
	}

	public void setPassFail(String passFail) {
		this.passFail = passFail;
	}

	@Override
	public String toString() {
		return "TestResult [densityAt15=" + densityAt15 + ", densityAt20=" + densityAt20 + ", commercialColour="
				+ commercialColour + ", doctorTest=" + doctorTest + ", appearance=" + appearance + ", testMethod="
				+ testMethod + ", requirement=" + requirement + ", result=" + result + ", passFail=" + passFail + "]";
	}

}
