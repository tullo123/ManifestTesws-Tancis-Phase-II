package com.ManifestTeswTancis.Entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@DynamicUpdate
@Table(name = "TANCISEXT.MANIFEST_APPROVAL_STATUS")
public class ManifestApprovalStatus {
	@Id
	@NotNull
	@Column(name = "MRN", unique = true)
	private String mrn;

	@Column(name = "VOYAGE_NO")
	private String voyageNumber;

	@Column(name = "IMO_NO")
	private String transportMeansId;

	@Column(name = "TPA_UID")
	private String communicationAgreedId;

	@Column(name = "APPROVED_STATUS")
	private boolean approvedStatus;

	@Column(name = "APPROVAL_NOTICE_STATUS")
	private boolean approvedNoticeStatus;

	@Column(name = "MRN_SENT")
	private boolean mrnStatus;

	@Column(name = "MRN_STATUS_FEEDBACK")
	private boolean mrnStatusFeedback;
	
	@Column(name = "PROCESSING_DT")
	private String approvalDt;
	
	@Column(name = "CONTROL_REFERENCE_NO")
	private String controlReferenceNumber;
	
	@Column(name = "PROCESSING_STATUS")
	private String processingStatus;

	@Column(name = "FIRST_REGISTER_DT", nullable = false, updatable = false)
	@CreationTimestamp
	private LocalDateTime firstRegisterDate;

	@Column(name = "LAST_UPDATE_DT")
	@UpdateTimestamp
	private LocalDateTime lastUpdateDate;

	@Column(name="EXPORT_MRN")
	private String mrnOut;

	@Column(name="CANCELLATION_DT")
	private String cancellationDate;

	@Column(name="CANCELLATION_STATUS")
	private String cancellationStatus;

	@Column(name="RECEIVED_NOTICE_SENT")
	private boolean receivedNoticeSent;

	@Column(name="RECEIVED_NOTICE_DT")
	private String noticeDate;

	@Column(name="REJECTED_YN")
	private String rejectedYN;

	@Column(name="EXPORT_APPROVED_STATUS")
	private boolean exportManifestApprovedStatus;

	@Column(name="EXPORT_APPROVED_DT")
	private String exportManifestApprovedDate;

	@Column(name="EXPORT_RECEIVED_STATUS")
	private boolean exportManifestReceivedStatus;

	@Column(name="EXPORT_RECEIVED_DT")
	private String exportManifestReceivedDate;

}
