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
public class EdNoticeId implements Serializable {
	private String documentCode;
	private String documentNumber;
	private String createDate;
	private Integer sequenceNumber;
}
