package com.ManifestTeswTancis.Entity;

import com.ManifestTeswTancis.idEntities.ExportChangeGeneralId;
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
import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@DynamicUpdate
@Table(name = "TANCISEXT.EX_DL_LOADING_CHANGE_GEN")
@IdClass(ExportChangeGeneralId.class)
public class ExportChangeGeneralEntity implements Serializable {
    @Id
    @Column(name="DECLARANT_TIN")
    private String declarantTin;

    @Column(name="DECLARANT_YEAR")
    private String declarantYear;

	@Column(name="PROCESS_TYPE")
    private String processType;

	@Column(name="DECLARANT_SERIAL_NO")
    private String declarantSerialNo;

	@Column(name="PROCESSING_STATUS")
    private String processingStatus;

	@Column(name="PROCESSING_DT")
    private Date processingDate;

	@Column(name="PROCESSING_ID")
    private String processingId;

	@Column(name="WAREHOUSE_CD")
    private String warehouseCode;

	@Column(name="DECLARATION_NO")
    private String declarationNo;

	@Column(name="MASTER_BL_NO")
    private String masterBillOfLading;

	@Column(name="HOUSE_BL_NO")
    private String houseBillOfLading;

	@Column(name="TARGET_DECLARATION_NO")
    private String targetDeclarationNo;

	@Column(name="TARGET_MASTER_BL_NO")
    private String targetMasterBlNo;

	@Column(name="TARGET_HOUSE_BL_NO")
    private String targetHouseBlNo;

	@Column(name="CUSTOMS_OFFICE_CD")
    private String customsOfficeCode;

	@Column(name="CHANGE_TYPE")
    private String changeType;

	@Column(name="CHANGE_REASON")
    private String changeReason;

	@Column(name="CHANGE_PACKAGE")
    private Integer changePackage;

	@Column(name="PACKAGE_UNIT")
    private String packageUnit;

	@Column(name="CHANGE_GROSS_WEIGHT")
    private Double changeGrossWeight;

	@Column(name="GROSS_WEIGHT_UNIT")
    private String changeGrossWeightUnit;

	@Column(name="CHANGE_NET_WEIGHT")
    private Double changeNetWeight;

	@Column(name="NET_WEIGHT_UNIT")
    private String netWeightUnit;

	@Column(name="AUDITOR")
    private String auditor;

	@Column(name="AUDIT_DT")
    private Date auditDate;

	@Column(name="AUDIT_COMMENT")
    private String auditComment;

	@Column(name="DECLARANT_NAME")
    private String declarantName;

	@Column(name="DECLARANT_TEL")
    private String declarantTel;

	@Column(name="FIRST_REGISTER_ID")
    private String firstRegisterId;

	@Column(name="FIRST_REGISTER_DT",nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime firstRegisterDate;

	@Column(name="LAST_UPDATE_ID")
    private String lastUpdateId;

	@Column(name="LAST_UPDATE_DT")
    @UpdateTimestamp
    private LocalDateTime lastUpdateDate;

	@Column(name="TARGET_CONTAINER_NO")
    private String targetContainerNo;

	@Column(name="CONTAINER_NO")
    private String containerNo;

	@Column(name="BL_CARRY_OUT_PACKAGE")
    private Integer blCarryOutPackage;

	@Column(name="BL_CARRY_OUT_WEIGHT")
    private Double blCarryOutWeight;

}
