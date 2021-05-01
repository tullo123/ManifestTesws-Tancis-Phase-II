package com.ManifestTeswTancis.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import com.ManifestTeswTancis.idEntities.SequenceId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "TANCISINT.PUMPING_SEQUENCE")
@IdClass(SequenceId.class)
public class PumpigSequenceEntity implements Serializable {
	
	@Id
	@Column(name = "TERMINAL")
	private String terminal;
    
	@Id
    @Column(name = "REFERENCE_NO")
	private String refNo;

	@Column(name = "QUANTITY")
	private Integer quantity;

	@Column(name = "CREATED_DT")
	private String createdDate;
}
