package com.ManifestTeswTancis.dtos;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PumpingSequenceDto implements Serializable {
	private static final long serialVersionUID = -7243803141042722111L;
	private String terminal;
	private Double quantity;
	private LocalDateTime startDateTime;
	private LocalDateTime endDateTime;
	private String status;
}
