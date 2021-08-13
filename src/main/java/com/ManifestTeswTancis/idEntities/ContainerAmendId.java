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
public class ContainerAmendId implements Serializable {
	private static final long serialVersionUID = 835843312327855507L;
	private String declarantTin;
	private String amendYear;
	private String processType;
	private String amendSerialNumber;
	private String billOfLading;
	private String containerNo;
}
