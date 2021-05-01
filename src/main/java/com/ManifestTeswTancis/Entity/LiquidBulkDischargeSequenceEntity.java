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
@Table(name = "TANCISINT.LIQUID_BK_DISCHARGE_SEQUENCE")
public class LiquidBulkDischargeSequenceEntity implements Serializable {
	private static final long serialVersionUID = 4071026957403609003L;
	@Id
	@Column(name = "REFERENCE_NO")
	private String refNo;

	@Column(name = "VOYAGE_NO")
	private String voyageNo;

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
