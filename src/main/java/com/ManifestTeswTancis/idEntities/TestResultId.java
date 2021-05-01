package com.ManifestTeswTancis.idEntities;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TestResultId implements Serializable {
	private static final long serialVersionUID = 3898015605093446873L;
	private String testResult;
	private String tbsCodeNo;
	

}
