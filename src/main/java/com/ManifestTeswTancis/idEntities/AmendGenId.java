package com.ManifestTeswTancis.idEntities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AmendGenId implements Serializable{
	private static final long serialVersionUID = -959893493030240780L;
	private String declarantTin;
	private String amendYear;
	private String processType;
	private String amendSerialNumber;
	private String amendType;
}
