package com.ManifestTeswTancis.Entity;

import com.ManifestTeswTancis.dtos.ExportContainersDto;
import com.ManifestTeswTancis.idEntities.ExportContainerId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@DynamicUpdate
@Table(name = "TANCISEXT.EX_MF_EXPORT_BL_CONTAINER")
@IdClass(ExportContainerId.class)
public class ExportContainerEntity implements Serializable {
    @Id
    @Column(name="MRN")
    private String mrn;

    @Id
    @Column(name="MASTER_BL_NO")
    private String masterBillOfLading;

    @Id
    @Column(name="HOUSE_BL_NO")
    private String houseBillOfLading;

    @Id
    @Column(name="CONTAINER_NO")
    private String containerNo;

    @Column(name="MSN")
    private String msn;

    @Column(name="HSN")
    private String hsn;

    @Column(name="CONTAINER_SIZE")
    private String containerSize;

    @Column(name="CONTAINER_TYPE")
    private String containerType;

    @Column(name="SEAL_NO_1")
    private String sealNumber;

    @Column(name="SEAL_NO_2")
    private String sealNumberTwo;

    @Column(name="SEAL_NO_3")
    private String sealNumberThree;

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

    @Column(name="FIRST_REGISTER_DT")
    @CreationTimestamp
    private LocalDateTime firstRegisterDt;

    @Column(name="LAST_UPDATE_ID")
    private String lastUpdateId;

    @Column(name="LAST_UPDATE_DT")
    @UpdateTimestamp
    private LocalDateTime lastUpdateDate;
}
