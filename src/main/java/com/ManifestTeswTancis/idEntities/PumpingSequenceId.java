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
public class PumpingSequenceId implements Serializable{
	private static final long serialVersionUID = -912602300331560417L;
	private String terminal;
	private String refNo;
	
}
