package com.ManifestTeswTancis.Entity;

import com.ManifestTeswTancis.idEntities.AmendItemContainerId;
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
@Table(name="TANCISEXT.EX_MF_IMPORT_AMEND_ITEM_CONT")
@IdClass(AmendItemContainerId.class)
public class ExImportAmendItemContainer implements Serializable {
	private static final long serialVersionUID = 736429517565329958L;
    @Id
	@NotNull
	@Column(name="DECLARANT_TIN")    
	private String declarantTin;
	
	@NotNull
	@Column(name="AMEND_YEAR")         
	private String amendYear;
	
	@NotNull
	@Column(name="PROCESS_TYPE")       
	private String processType;
	
	@NotNull
	@Column(name="AMEND_SERIAL_NO")     
	private String amendSerialNumber;
	
	@NotNull
	@Column(name="ITEM_NO")           
	private String itemNumber;
	
	@NotNull
	@Column(name="CONTAINER_NO")    
	private String containerNumber;

	@Column(name="CONTAINER_SIZE")  
	private String containerSize;

	@Column(name="CONTAINER_TYPE")           
	private String containerType;

	@Column(name="SEAL_NO_1")             
	private String sealNumberOne;

	@Column(name="SEAL_NO_2") 
	private String sealNumberTwo;

	@Column(name="SEAL_NO_3")       
	private String sealNumberThree;

	@Column(name="CUSTOMS_SEAL_NO_1")      
	private String customSealNumberOne;

	@Column(name="CUSTOMS_SEAL_NO_2")       
	private String customSealNumberTwo;

	@Column(name="CUSTOMS_SEAL_NO_3")
	private String customSealNumberThree;

	@Column(name="CONTAINER_PACKAGE")         
	private Integer containerPackage;

	@Column(name="PACKAGE_UNIT")               
	private String packageUnit;
	
	@Column(name="CONTAINER_WEIGHT")        
	private Double containerWeight;

	@Column(name="WEIGHT_UNIT")               
	private String weightUnit;

	@Column(name="CONTAINER_VOLUME")         
	private Double containerVolume;

	@Column(name="VOLUME_UNIT")         
	private String volumeUnit;

	@Column(name="FREIGHT_INDICATOR")         
	private String freightIndicator;
	
	@Column(name="MINMUM_TEMPERATURE")       
	private Double minimumTemperature;

	@Column(name="MAXMUM_TEMPERATURE")         
	private Double maximumTemperature;
	
	@Column(name="REFER_PLUG_YN")              
	private String referPlugYn;

	@Column(name="IMDG_CD")           
	private String imdgCode;

	@Column(name="FIRST_REGISTER_ID")        
	private String firstRegisterId;

	@Column(name="FIRST_REGISTER_DT", nullable = false, updatable = false)
	@CreationTimestamp
	private Date firstRegisterDate;
	
	@Column(name="LAST_UPDATE_ID")              
	private String lastUpdateId;

	@Column(name="LAST_UPDATE_DT")
	@UpdateTimestamp
	private Date lastUpdateDate;
	
	@Column(name="OLD_CONTAINER_NO")            
	private String oldContainerNumber;
}
