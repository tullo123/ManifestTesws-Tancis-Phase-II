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
public class ContainerId implements Serializable {
	private static final long serialVersionUID = 2492961508424042418L;
	private String mrn;
	private String masterBillOfLading;
	private String containerNo;
	private String houseBillOfLading;
}
