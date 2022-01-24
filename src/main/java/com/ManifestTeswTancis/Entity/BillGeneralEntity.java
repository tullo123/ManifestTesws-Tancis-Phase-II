package com.ManifestTeswTancis.Entity;

import com.ManifestTeswTancis.idEntities.BillGeneralId;
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
@Table(name = "TANCISINT.CL_FI_BILL_GENERAL")
@IdClass(BillGeneralId.class)
public class BillGeneralEntity implements Serializable {
    @Id
    @NotNull
    @Column(name="BILL_CUSTOMS_OFFICE_CD")
    private String billCustomsOfficeCd;

    @NotNull
    @Column(name="BILL_YY")
    private String billYy;

    @NotNull
    @Column(name="BILL_TYPE_CD")
    private String billTypeCd;

    @NotNull
	@Column(name="BILL_SERIAL_NO")
    private String billSerialNo;

    @NotNull
	@Column(name="BILL_DG")
    private Double billDg;

	@Column(name="REFERENCE_TYPE")
    private String referenceType;

	@Column(name="REFERENCE_KEY1")
    private String referenceKeyOne;

	@Column(name="REFERENCE_KEY2")
    private String referenceKeyTwo;

	@Column(name="REFERENCE_KEY3")
    private String referenceKeyThree;

	@Column(name="REFERENCE_KEY4")
    private String referenceKeyFour;

	@Column(name="OIL_DECLARATION_YN")
    private String oilDeclarationYn;

	@Column(name="BILL_DT")
    private Date billDate;

	@Column(name="PAYMENT_DEADLINE_DAY")
    private String paymentDeadlineDay;

	@Column(name="LAST_DEADLINE_EXTENSION_DAY")
    private String lastDeadlineExtensionDay;

	@Column(name="LAST_PAYMENT_DEADLINE_DAY")
    private String lastPaymentDeadlineDay;

	@Column(name="LAST_DEADLINE_EXTENSION_REASON")
    private String lastDeadlineExtensionReason;

	@Column(name="DISPOSAL_YN")
    private String disposalYn;

	@Column(name="DISPOSAL_DT")
    private Date disposalDate;

	@Column(name="DISPOSAL_CD")
    private String disposalCd;

	@Column(name="DISPOSAL_REMARK")
    private String disposalRemark;

	@Column(name="PAYER_TYPE_CD")
    private String payerTypeCd;

	@Column(name="PAYER_TIN")
    private String payerTin;

	@Column(name="PAYER_NAME")
    private String payerName;

	@Column(name="DUTY_AMT")
    private Double dutyAmt;

	@Column(name="VAT_AMT")
    private Double vatAmt;

	@Column(name="OTHER_AMT")
    private Double otherAmt;

	@Column(name="FEE_AMT")
    private Double feeAmt;

	@Column(name="PENALTY_AMT")
    private Double penaltyAmt;

	@Column(name="TOTAL_BILL_TAX_AMT")
    private Double totalBillTaxAmt;

	@Column(name="REVEVUE_GATEWAY_EXIST")
    private String revenueGatewayExist;

	@Column(name="PAYMENT_BANK_CD")
    private String paymentBankCd;

	@Column(name="PAYMENT_BANK_ACCOUNT_NO")
    private String paymentBankAccountNo;

	@Column(name="CCE_BANK_CD")
    private String cceBankCd;

	@Column(name="CCE_BANK_ACCOUNT_NO")
    private String cceBankAccountNo;

	@Column(name="RECEIPT_TYPE_CD")
    private String receiptTypeCd;

	@Column(name="TREASURY_VOUCHER_NO")
    private String treasuryVoucherNo;

	@Column(name="RECEIPT_DT")
    private Date receiptDt;

	@Column(name="RECEIPT_AMT")
    private Double receiptAmt;

	@Column(name="RECEIPT_BANK_CD")
    private String receiptBankCd;

	@Column(name="RECEIPT_BANK_ACCOUNT_NO")
    private String receiptBankAccountNo;

	@Column(name="RECEIPT_NO")
    private String receiptNo;

	@Column(name="MANUAL_RECEIPT_REASON")
    private String manualReceiptReason;

	@Column(name="BILL_REGISTER_ID")
    private String billRegisterId;

	@Column(name="BILL_STATUS_CD")
    private String billStatusCd;

	@Column(name="DELINQUENCY_YN")
    private String delinquencyYn;

	@Column(name="ERROR_RECEIVE_DT")
    private Date errorReceiveDt;

	@Column(name="ERROR_TYPE_CD")
    private String errorTypeCd;

	@Column(name="RECEIPT_REGISTER_DT")
    private Date receiptRegisterDt;

	@Column(name="ERROR_MESSAGE")
    private String errorMessage;

	@Column(name="SEND_STATUS_CD")
    private String sendStatusCd;

	@Column(name="FIRST_REGISTER_ID")
    private String firstRegisterId;

	@Column(name="FIRST_REGISTER_DT")
    private Date firstRegisterDt;

	@Column(name="LAST_UPDATE_ID")
    private String lastUpdateId;

	@Column(name="LAST_UPDATE_DT")
    private Date lastUpdateDate;

	@Column(name="RECEIPT_REGISTER_ID")
    private String receiptRegisterId;
}
