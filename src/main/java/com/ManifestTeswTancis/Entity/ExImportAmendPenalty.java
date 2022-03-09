package com.ManifestTeswTancis.Entity;

import com.ManifestTeswTancis.idEntities.AmendPenaltyId;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "TANCISEXT.EX_MF_IMPORT_AMEND_PENALTY")
@IdClass(AmendPenaltyId.class)
public class ExImportAmendPenalty implements Serializable {
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

	@NotNull
	@Column(name = "PENALTY_TYPE")
	private String penaltyType;

	@Column(name = "AMOUNT_TZS")
	private Double amountTzs;

	@Column(name = "AMOUNT_USD")
	private Double amountUsd;

	@NotNull
	@Column(name = "INVOICE_NO")
	private String invoiceNumber;

	@Column(name = "INVOICE_DT")
	private Date invoiceDate;

	@Column(name = "PAY_EXPIRE_DT")
	private Date payExpireDate;

	@NotNull
	@Column(name = "ACCOUNT_NO")
	private String accountNumber;

	@Column(name = "DECLARANT_CD")
	private String declarantCode;

	@Column(name = "CUSTOMS_OFFICE_CD")
	private String customOfficeCode;

	@Column(name = "RECEIPT_DATE")
	private String receiptDate;

	@Column(name = "EXTERNAL_YN")
	private String externalYn;

	@Column(name = "BILL_NO")
	private String billNumber;
}
