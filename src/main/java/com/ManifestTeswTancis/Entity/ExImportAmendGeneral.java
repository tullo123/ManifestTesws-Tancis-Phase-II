package com.ManifestTeswTancis.Entity;

import com.ManifestTeswTancis.idEntities.AmendGenId;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "TANCISEXT.EX_MF_IMPORT_AMEND_GENERAL")
@IdClass(AmendGenId.class)
public class ExImportAmendGeneral implements Serializable {
	@Id
	@NotNull
	@Column(name = "DECLARANT_TIN")
	private String declarantTin;

	@NotNull
	@Column(name = "AMEND_YEAR")
	private String amendYear;

	@NotNull
	@Column(name = "PROCESS_TYPE")
	private String processType;

	@NotNull
	@Column(name = "AMEND_SERIAL_NO")
	private String amendSerialNumber;

	@Column(name = "PROCESSING_STATUS")
	private String processingStatus;

	@Column(name = "PROCESSING_DT")
	private Date processingDate;

	@Column(name = "PROCESSING_ID")
	private String processingId;

	@Column(name = "CUSTOMS_OFFICE_CD")
	private String customOfficeCode;

	@Column(name = "MRN")
	private String mrn;

	@Column(name = "MSN")
	private String msn;

	@Column(name = "HSN")
	private String hsn;

	@NotNull
	@Column(name = "AMEND_TYPE")
	private String amendType;

	@Column(name = "AMEND_REASON_CD")
	private String amendReasonCode;

	@Column(name = "AMEND_REASON_COMMENT")
	private String amendReasonComment;

	@Column(name = "BILL_YN")
	private String billYn;

	@Column(name = "BILL_NO")
	private String billNumber;

	@Column(name = "BILL_ISSUE_DT")
	private Date billIssueDate;

	@Column(name = "PAYMENT_DT")
	private Date paymentDate;

	@Column(name = "PENALTY_AMT")
	private Double penaltyAmount;

	@Column(name = "SUBMIT_DT")
	private Date submitDate;

	@Column(name = "AUDITOR")
	private String auditor;

	@Column(name = "AUDIT_DT")
	private Date auditDate;

	@Column(name = "AUDIT_COMMENT")
	private String auditComment;

	@Column(name = "DECLARANT_CD")
	private String declarantCode;

	@Column(name = "DECLARANT_NAME")
	private String declarantName;

	@Column(name = "DECLARANT_TEL")
	private String declarantTel;

	@Column(name = "FIRST_REGISTER_ID")
	private String firstRegisterId;

	@Column(name = "FIRST_REGISTER_DT", nullable = false, updatable = false)
	@CreationTimestamp
	private LocalDateTime firstRegisterDate;

	@Column(name = "LAST_UPDATE_ID")
	private String lastUpdateId;

	@Column(name = "LAST_UPDATE_DT")
	@UpdateTimestamp
	private LocalDateTime lastUpdateDate;
}
