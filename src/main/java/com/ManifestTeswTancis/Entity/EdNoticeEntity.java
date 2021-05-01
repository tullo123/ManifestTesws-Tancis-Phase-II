package com.ManifestTeswTancis.Entity;

import com.ManifestTeswTancis.idEntities.EdNoticeId;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "TANCISEXT.EX_ED_NOTICE")
@IdClass(EdNoticeId.class)

public class EdNoticeEntity {

	@NotNull
	@Column(name = "DOCUMENT_CD")
	private String documentCode;
	@Id
	@NotNull
	@Column(name = "DOCUMENT_NO")
	private String documentNumber;

	@NotNull
	@Column(name = "CREATE_DT")
	private String createDate;

	@NotNull
	@Column(name = "SEQUENCE_NO")
	private Integer sequenceNumber;

	@Column(name = "DOCUMENT_FUNCTION_TYPE")
	private String documentFunctionType;

	@Column(name = "CUSTOMS_OFFICE_CD")
	private String customsOfficeCode;

	@Column(name = "RECEIVER_ID")
	private String receiverId;

	@Column(name = "RECEIVER_SUB_ID")
	private String receiverSubId;

	@Column(name = "SENDER_ID")
	private String senderId;

	@Column(name = "SENDER_SUB_ID")
	private String senderSubId;

	@Column(name = "DOCUMENT_TYPE")
	private String documentType;

	@Column(name = "ORIGINAL_DOCUMENT_CD")
	private String OriginalDocumentCode;

	@Column(name = "ORIGINAL_DOCUMENT_NO")
	private String originalDocumentNumber;

	@NotNull
	@Column(name = "TRANSFER_TYPE")
	private String transferType;

	@Column(name = "PROCESS_START_DT")
	private Date processStartDate;

	@Column(name = "PROCESS_END_DT")
	private Date processEndDate;

	@NotNull
	@Column(name = "PROCESS_STATUS")
	private String processingStatus;

	@Column(name = "REFERENCE_INFORMATION_1")
	private String referenceInformationOne;

	@Column(name = "REFERENCE_INFORMATION_2")
	private String referenceInformationTwo;

	@Column(name = "REFERENCE_INFORMATION_3")
	private String referenceInformationThree;

	@Column(name = "REFERENCE_INFORMATION_4")
	private String referenceInformationFour;

	@Column(name = "REFERENCE_INFORMATION_5")
	private String referenceInformationFive;

}
