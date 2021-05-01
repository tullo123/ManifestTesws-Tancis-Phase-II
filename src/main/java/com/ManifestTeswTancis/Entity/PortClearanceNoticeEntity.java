package com.ManifestTeswTancis.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "TANCISEXT.EX_CL_PORT_CLEARANCE_NOTICE")
public class PortClearanceNoticeEntity implements Serializable {
	private static final long serialVersionUID = -6358058296981557909L;
	
    @Id
	@Column(name = "CALL_ID")
	private String callId;

	@Column(name = "CLEARANCE_REFERENCE")
	private String clearanceReference;

	@Column(name = "APPROVAL_STATUS")
	private String approvalStatus;

	@Column(name = "COMMENTS")
	private String comment;

	@Column(name = "NOTICE_DATE")
	private String noticeDate;

}
