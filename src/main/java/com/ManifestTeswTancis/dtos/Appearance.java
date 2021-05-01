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
public class Appearance implements Serializable {
	private static final long serialVersionUID = 2705533885448934090L;
	private String testMethod;
	private String requirement;
	private String result;
	private String passFail;

}
