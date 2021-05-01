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
public class DensityAt20 implements Serializable {
	private static final long serialVersionUID = -758147814144311324L;
	private String testMethod;
	private String requirement;
	private String result;
	private String passFail;

}
