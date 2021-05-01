package com.ManifestTeswTancis.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ResponseFromTesws {
	private String preparationDateTime;
	private String controlReferenceNumber;
	private String messageTypeId;
	private String receiverMessageReferenceNumber;
	private boolean error;
	List<ErrorClass> responses;
}
