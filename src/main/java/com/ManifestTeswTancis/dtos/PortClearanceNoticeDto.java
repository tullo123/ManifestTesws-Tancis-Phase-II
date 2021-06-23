package com.ManifestTeswTancis.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PortClearanceNoticeDto implements Serializable {
	private static final long serialVersionUID = 9099012099890704114L;
	private String communicationAgreedId;
	private String clearanceRef;
	private String approvalStatus;
	private String comment;
	private String noticeDate;
}
