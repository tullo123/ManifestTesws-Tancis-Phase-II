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
public class SequenceId implements Serializable {
	
	private static final long serialVersionUID = 6606465470534054568L;
	private String terminal;
	private String refNo;
	
	
	
}
