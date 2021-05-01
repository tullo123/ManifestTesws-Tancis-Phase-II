package com.ManifestTeswTancis.Entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.ManifestTeswTancis.idEntities.PumpingSequenceId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name="TANCISINT.PUMPING_SEQUENCE_UPD")
@IdClass(PumpingSequenceId.class)
public class PumpingSequenceDtoEntity implements Serializable {
	private static final long serialVersionUID = 8848867442205057694L;

	@Id
	@Column(name="TERMINAL")
	private String terminal;
	
	@Id
	@Column(name = "REFERENCE_NO")
	private String refNo;
	
	@Column(name="QUANTITY")
	private Integer quantity;
	
	@Column(name="FROM_DATE")
	private String fromDt;
	
	@Column(name="TO_DATE")
	private String toDt;
	
	@Column(name="DURATION")
	private String duration;
	
	@Column(name="CREATED_AT")
	private String createdAt;
}
