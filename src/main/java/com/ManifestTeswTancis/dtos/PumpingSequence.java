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
public class PumpingSequence implements Serializable {
	private static final long serialVersionUID = 5651222215840609015L;
	private String terminal;
	private Double quantity;
	private LocalDateTime startDateTime;
	private LocalDateTime endDateTime;
	private String status;
    

}
