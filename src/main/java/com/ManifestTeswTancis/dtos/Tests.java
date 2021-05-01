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
public class Tests implements Serializable {
	private static final long serialVersionUID = 8926773317345367046L;
	private String testMethod;
	private String requirement;
	private String result;
	private String passFail;

}
