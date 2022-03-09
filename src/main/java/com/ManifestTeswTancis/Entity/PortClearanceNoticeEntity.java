package com.ManifestTeswTancis.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
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
	@Column(name = "TPA_UID")
	private String communicationAgreedId;

	@Column(name = "CLEARANCE_REFERENCE")
	private String clearanceRef;

	@Column(name = "APPROVAL_STATUS")
	private String approvalStatus;

	@Column(name = "COMMENTS")
	private String comment;

	@Column(name = "NOTICE_DATE")
	private String noticeDate;

	@Column(name="FIRST_REGISTER_DT", nullable = false, updatable = false)
	@CreationTimestamp
	private LocalDateTime firstRegisterDate;

	@Column(name = "FIRST_REGISTER_ID")
	private String firstRegisterId;

	@Column(name = "LAST_UPDATE_DT")
	@UpdateTimestamp
	private LocalDateTime lastUpdateDate;

	@Column(name = "LAST_UPDATE_ID")
	private String lastUpdateId;

}
