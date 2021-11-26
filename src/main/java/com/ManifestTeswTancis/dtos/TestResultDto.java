package com.ManifestTeswTancis.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TestResultDto implements Serializable {
	private String testMethod;
	private String requirement;
	private String result;
	private String passFail;
	private TestsDto densityAt15;
	private TestsDto densityAt20;
	private TestsDto commercialColour;
	private TestsDto doctorTest;
	private TestsDto appearance;
}
