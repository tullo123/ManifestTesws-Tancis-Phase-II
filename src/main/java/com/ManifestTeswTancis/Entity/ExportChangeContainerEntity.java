package com.ManifestTeswTancis.Entity;

import com.ManifestTeswTancis.idEntities.ExportChangeContainerId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@DynamicUpdate
@Table(name = "TANCISEXT.EX_DL_LOADING_CHANGE_CONT")
@IdClass(ExportChangeContainerId.class)
public class ExportChangeContainerEntity implements Serializable {
    @Id
    @Column(name="DECLARANT_TIN")
    private String declarantTin;

    @Column(name="DECLARANT_YEAR")
    private String declarantYear;

    @Column(name="PROCESS_TYPE")
    private String processType;

    @Column(name="DECLARANT_SERIAL_NO")
    private String declarantSerialNo;

    @Column(name="WAREHOUSE_CD")
    private String warehouseCode;

    @Column(name="DECLARATION_NO")
    private String declarationNo;

    @Column(name="MASTER_BL_NO")
    private String masterBillOfLading;

    @Column(name="HOUSE_BL_NO")
    private String houseBillOfLading;

    @Column(name="CONTAINER_NO")
    private String containerNo;

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

    @Column(name="FIRST_REGISTER_ID")
    private String firstRegisterId;

    @Column(name="FIRST_REGISTER_DT")
    private LocalDateTime firstRegisterDate;

    @Column(name="LAST_UPDATE_ID")
    private String lastUpdateId;

    @Column(name="LAST_UPDATE_DT")
    private LocalDateTime lastUpdateDate;

    @Column(name="FREIGHT_INDICATOR")
    private String freightIndicator;
}
