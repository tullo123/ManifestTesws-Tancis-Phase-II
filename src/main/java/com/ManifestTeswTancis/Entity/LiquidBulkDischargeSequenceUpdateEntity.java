package com.ManifestTeswTancis.Entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "TANCISINT.LQD_BK_DISCHARGE_SEQUENCE_UPD")
public class LiquidBulkDischargeSequenceUpdateEntity implements Serializable {
	private static final long serialVersionUID = -1521163699945161097L;

	@Id
	@Column(name = "REFERENCE_NO")
	private String refNo;

	@Column(name = "VOYAGE_NO")
	private String voyageNo;

	@Column(name = "MRN")
	private String mrn;

	@Column(name = "CALL_ID")
	private String call_id;

	@Column(name = "VESSEL_NAME")
	private String vesselName;

	@Column(name = "IMO_NO")
	private String imoNo;

	@Column(name = "DESTINATION_PORT")
	private String destinationPort;

	@Column(name = "REFERENCE_DATE")
	private String refdt;

	@Column(name = "BL_QUANTITY")
	private Integer blQnt;

	@Column(name = "PRODUCT")
	private String product;
	
}
