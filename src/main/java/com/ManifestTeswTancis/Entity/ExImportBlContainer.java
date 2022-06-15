package com.ManifestTeswTancis.Entity;

import com.ManifestTeswTancis.idEntities.ContainerId;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "TANCISEXT.EX_MF_IMPORT_BL_CONTAINER")
@IdClass(ContainerId.class)
public class ExImportBlContainer {

	@Id
	@NotNull
	@Column(name = "MRN")
	private String mrn;

	@NotNull
	@Column(name = "MASTER_BL_NO")
	private String masterBillOfLading;

	@NotNull
	@Column(name = "HOUSE_BL_NO")
	private String houseBillOfLading;

	@NotNull
	@Column(name = "CONTAINER_NO")
	private String containerNo;

	@Column(name = "MSN")
	private String msn;

	@Column(name = "HSN")
	private String hsn;

	@Column(name = "CONTAINER_SIZE")
	private String containerSize;

	@Column(name = "CONTAINER_TYPE")
	private String typeOfContainer;

	@Column(name = "SEAL_NO_1")
	private String sealNumber;

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

	@Column(name = "FIRST_REGISTER_DT",nullable = false, updatable = false)
	@CreationTimestamp
	private LocalDateTime firstRegisterDate;

	@Column(name = "FIRST_REGISTER_ID")
	private String firstRegisterId;

	@Column(name = "LAST_UPDATE_ID")
	private String lastUpdateId;

	@Column(name = "LAST_UPDATE_DT")
	@UpdateTimestamp
	private LocalDateTime lastUpdateDate;

	@Column(name="CONTAINER_PACKAGE")
	private Integer containerPackage;

	@Column(name="PACKAGE_UNIT")
	private String packageUnit;

}
