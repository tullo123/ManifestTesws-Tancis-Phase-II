package com.ManifestTeswTancis.Entity;

import com.ManifestTeswTancis.idEntities.ExportCarryInBlId;
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
@Table(name = "TANCISEXT.EX_IO_EXPORT_EXP_CARRY_IN_BL")
@IdClass(ExportCarryInBlId.class)
public class ExportCarryInBlEntity implements Serializable {
    @Id
    @Column(name="EXP_CARRY_IN_MASTER_BL_NO")
    private String expectedCarryInMasterBlNo;

    @Id
    @Column(name="EXP_CARRY_IN_HOUSE_BL_NO")
    private String expectedCarryInHouseBlNo;

    @Id
	@Column(name="CARRY_IN_TYPE")
    private String carryInType;

    @Id
	@Column(name="EXP_CARRY_IN_BASE_NO")
    private String expectedCarryInBaseNo;

    @Id
	@Column(name="WAREHOUSE_CD")
    private String warehouseCode;

	@Column(name="PROCESSING_STATUS")
    private String processingStatus;

	@Column(name="PROCESSING_DT")
    private Date processingDate;

	@Column(name="PROCESSING_UNIT")
    private String processingUnit;

	@Column(name="APPROVAL_DT")
    private Date approvalDate;

	@Column(name="TRANSFER_END_DATE")
    private String transferEndDate;

	@Column(name="EXPECTED_PACKAGE")
    private Integer expectedPackage;

	@Column(name="PACKAGE_UNIT")
    private String packageUnit;

	@Column(name="EXPECTED_WEIGHT")
    private Double expectedWeight;

	@Column(name="WEIGHT_UNIT")
    private String weightUnit;

	@Column(name="BOND_VALUE")
    private Double bondValue;

	@Column(name="BOND_TYPE")
    private String bondType;

	@Column(name="BOND_NO")
    private String bondNo;

	@Column(name="CARRY_IN_REPORT_YN")
    private String carryInReportYn;

	@Column(name="EXIT_CONFIRM_YN")
    private String exitConfirmYn;

	@Column(name="ENTRY_CONFIRM_YN")
    private String entryConfirmYn;

	@Column(name="FIRST_REGISTER_ID")
    private String firstRegisterId;

	@Column(name="FIRST_REGISTER_DT")
    @CreationTimestamp
    private LocalDateTime firstRegisterDate;

	@Column(name="LAST_UPDATE_ID")
    private String lastUpdateId;

	@Column(name="LAST_UPDATE_DT")
    @UpdateTimestamp
    private LocalDateTime lastUpdateDate;

	@Column(name="BARCODE")
    private String barcode;

	@Column(name="SCANNER_PROCESSING_YN")
    private String scannerProcessingYn;

	@Column(name="SCANNER_PROCESSING_DT")
    private Date scannerProcessingDate;

	@Column(name="SCANNER_VERDICT_STATUS")
    private String scannerVerdictStatus;

	@Column(name="SCANNER_VERDICT_DT")
    private Date scannerVerdictDate;

	@Column(name="SCANNER_VERDICT_COMMENT")
    private String scannerVerdictComment;

	@Column(name="SCANNER_VERDICT_OFFICER_ID")
    private String scannerVerdictOfficerId;

	@Column(name="SCANNER_INSPECTION_STATUS")
    private String scannerInspectionStatus;

	@Column(name="SCANNER_INSPECTION_DT")
    private Date scannerInspectionDate;

	@Column(name="SCANNER_INSPECTION_COMMENT")
    private String scannerInspectionComment;

	@Column(name="SCANNER_INSPECTION_ID")
    private String scannerInspectionId;
}
