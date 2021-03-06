package com.ManifestTeswTancis.Entity;

import com.ManifestTeswTancis.idEntities.AmendItemId;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "TANCISEXT.EX_MF_IMPORT_AMEND_ITEM")
@IdClass(AmendItemId.class)
public class ExImportAmendItem implements Serializable{
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
	@Column(name = "ITEM_NO")
	private String itemNumber;

	@Column(name = "BEFORE_ITEM_COMMENTS")
	private String beforeItemComments;

	@Column(name = "AFTER_ITEM_COMMENTS")
	private String afterItemComments;

	@Column(name = "FIRST_REGISTER_ID")
	private String firstRegisterId;

	@Column(name = "FIRST_REGISTER_DT", nullable = false, updatable = false)
	@CreationTimestamp
	private Date firstRegisterDate;

	@Column(name = "LAST_UPDATE_ID")
	private String lastUpdateId;

	@Column(name = "LAST_UPDATE_DT")
	@UpdateTimestamp
	private Date lastUpdateDate;
}
