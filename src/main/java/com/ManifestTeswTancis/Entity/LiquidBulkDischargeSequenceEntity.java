package com.ManifestTeswTancis.Entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@DynamicUpdate
@Table(name = "TANCISINT.CM_LQD_BK_DISCHARGE_SEQUENCE")
public class LiquidBulkDischargeSequenceEntity implements Serializable {

	@Id
	@Column(name = "REFERENCE_NO")
	private String refNo;

	@Column(name = "VOYAGE_NO")
	private String voyageNo;

	@Column(name = "MRN")
	private String mrn;

	@Column(name = "TPA_UID")
	private String communicationAgreedId;

	@Column(name = "VESSEL_NAME")
	private String vesselName;

	@Column(name = "IMO_NO")
	private String imoNo;

	@Column(name="CALL_SIGN")
	private String callSign;

	@Column(name = "DESTINATION_PORT")
	private String destinationPort;

	@Column(name = "REFERENCE_DT")
	private LocalDateTime refDate;

	@Column(name = "BL_QUANTITY")
	private Double blQnt;

	@Column(name="OIL_TYPE")
	private String oilType;

	@Column(name="TERMINAL")
	private String terminal;

	@Column(name="PUMPING_SEQ_QUANTITY")
	private Double quantity;

	@Column(name="PUMPING_SEQ_START_DT")
	private LocalDateTime startDateTime;

	@Column(name="PUMPING_SEQ_END_DT")
	private LocalDateTime endDateTime;

	@Column(name="STATUS")
	private String status;

	@Column(name="FIRST_REGISTER_DT")
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
