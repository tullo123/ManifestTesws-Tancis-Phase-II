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
public class PumpSeq implements Serializable {
	private static final long serialVersionUID = -6305301473274203485L;
	private String terminal;
	private Integer quantity;

}
