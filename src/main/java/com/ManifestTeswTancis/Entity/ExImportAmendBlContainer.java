package com.ManifestTeswTancis.Entity;

import com.ManifestTeswTancis.idEntities.ContainerAmendId;
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
@Table(name = "TANCISEXT.EX_MF_IMPORT_AMEND_BL_CONT")
@IdClass(ContainerAmendId.class)
public class ExImportAmendBlContainer implements Serializable {
	private static final long serialVersionUID = 3300232137526405825L;

	@Column(name = "DECLARANT_TIN")
	private String declarantTin;

	@Column(name = "AMEND_YEAR")
	private String amendYear;

	@Column(name = "PROCESS_TYPE")
	private String processType;

	@Column(name = "AMEND_SERIAL_NO")
	private String amendSerialNumber;

	@Id
	@Column(name = "BL_NO")
	private String billOfLading;

	@Id
	@Column(name = "CONTAINER_NO")
	private String containerNo;

	@Column(name = "CONTAINER_SIZE")
	private String containerSize;

	@Column(name = "CONTAINER_TYPE")
	private String typeOfContainer;

	@Column(name = "SEAL_NO_1")
	private String sealNumberOne;

	@Column(name = "SEAL_NO_2")
	private String sealNumberTwo;

	@Column(name = "SEAL_NO_3 ")
	private String sealNumberThree;

	@Column(name = "CUSTOMS_SEAL_NO_1")
	private String customSealNumberOne;

	@Column(name = "CUSTOMS_SEAL_NO_2")
	private String customSealNumberTwo;

	@Column(name = "CUSTOMS_SEAL_NO_3")
	private String customSealNumberThree;

	@Column(name = "FREIGHT_INDICATOR ")
	private String freightIndicator;

	@Column(name = "CONTAINER_VOLUME")
	private Double volume;

	@Column(name = "VOLUME_UNIT")
	private String volumeUnit;

	@Column(name = "CONTAINER_WEIGHT")
	private Double weight;

	@Column(name = "WEIGHT_UNIT ")
	private String weightUnit;

	@Column(name = "REFER_PLUG_YN")
	private String referPlugYn;

	@Column(name = "MINMUM_TEMPERATURE")
	private Double minimumTemperature;

	@Column(name = "MAXMUM_TEMPERATURE")
	private Double maximumTemperature;

	@Column(name = "IMDG_CD")
	private String imdgCd;

	@Column(name = "FIRST_REGISTER_ID")
	private String firstRegisterId;

	@Column(name = "LAST_UPDATE_ID")
	private Date lastUpdateId;

	@Column(name = "LAST_UPDATE_DT")
	private Date lastUpdateDate;

}
