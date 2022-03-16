package com.ManifestTeswTancis.Entity;

import com.ManifestTeswTancis.dtos.ExportBillOfLadingDto;
import com.ManifestTeswTancis.idEntities.ExportHouseBlId;
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
@Table(name = "TANCISEXT.EX_MF_EXPORT_HOUSE_BL")
@IdClass(ExportHouseBlId.class)
public class ExportHouseBlEntity implements Serializable {
    @Id
    @Column(name="MRN")
    private String mrn;

    @Id
    @Column(name="MASTER_BL_NO")
    private String masterBillOfLading;

    @Id
    @Column(name="HOUSE_BL_NO")
    private String houseBillOfLading;

    @Column(name="MSN")
    private String msn;

    @Column(name="HSN")
    private String hsn;

    @Column(name="CARGO_CLASSIFICATION")
    private String tradeType;

    @Column(name="EXPORTER_TIN")
    private String exporterTin;

    @Column(name="EXPORTER_NAME")
    private String exporterName;

    @Column(name="EXPORTER_TEL")
    private String exporterTel;

    @Column(name="EXPORTER_ADDRESS")
    private String exporterAddress;

    @Column(name="CONSIGNEE_TIN")
    private String consigneeTin;

    @Column(name="CONSIGNEE_NAME")
    private String consigneeName;

    @Column(name="CONSIGNEE_TEL")
    private String consigneeTel;

    @Column(name="CONSIGNEE_ADDRESS")
    private String consigneeAddress;

    @Column(name="NOTIFY_TIN")
    private String notifyTin;

    @Column(name="NOTIFY_NAME")
    private String notifyName;

    @Column(name="NOTIFY_TEL")
    private String notifyTel;

    @Column(name="NOTIFY_ADDRESS")
    private String notifyAddress;

    @Column(name="GOODS_DESCRIPTION")
    private String blDescription;

    @Column(name="BL_PACKAGE")
    private Double blPackage;

    @Column(name="PACKAGE_UNIT")
    private String packageUnit;

    @Column(name="BL_GROSS_WEIGHT")
    private Double blGrossWeight;

    @Column(name="GROSS_WEIGHT_UNIT")
    private String grossWeightUnit;

    @Column(name="BL_NET_WEIGHT")
    private Double blNetWeight;

    @Column(name="NET_WEIGHT_UNIT")
    private String netWeightUnit;

    @Column(name="BL_GROSS_VOLUME")
    private Double blVolume;

    @Column(name="VOLUME_UNIT")
    private String volumeUnit;

    @Column(name="DESTINATION_PLACE")
    private String placeOfDestination;

    @Column(name="IMDG_CD")
    private String imdgCode;

    @Column(name="INVOICE_VALUE")
    private Double invoiceValue;

    @Column(name="INVOICE_CURRENCY")
    private String invoiceCurrency;

    @Column(name="FREIGHT_CHARGE")
    private Double freightCharge;

    @Column(name="FREIGHT_CURRENCY")
    private String freightCurrency;

    @Column(name="PACKING_TYPE")
    private String packingType;

    @Column(name="OIL_TYPE")
    private String oilType;

    @Column(name="BILL_YN")
    private String billYn;

    @Column(name="BILL_NO")
    private String billNo;

    @Column(name="BILL_ISSUE_DT")
    private Date billIssueDate;

    @Column(name="PAYMENT_DT")
    private Date paymentDate;

    @Column(name="SHIPPING_MARKS")
    private String marksNumbers;

    @Column(name="CONTAINER_COUNT")
    private Integer containerCount;

    @Column(name="AUDIT_STATUS")
    private String auditStatus;

    @Column(name="AUDITOR")
    private String auditor;

    @Column(name="AUDIT_DT")
    private Date auditDate;

    @Column(name="AUDIT_COMMENT")
    private String auditComment;

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

    public ExportHouseBlEntity(ExportBillOfLadingDto exportBillOfLadingDto){
        this.houseBillOfLading = exportBillOfLadingDto.getHouseBillOfLading();
        this.masterBillOfLading = exportBillOfLadingDto.getMasterBillOfLading();
        if(exportBillOfLadingDto.getExporterName()==null){this.exporterName=exportBillOfLadingDto.getConsignorName();}
        this.exporterName = exportBillOfLadingDto.getExporterName();
        if (exportBillOfLadingDto.getExporterTel()==null){this.exporterTel=exportBillOfLadingDto.getConsignorTel();}
        this.exporterTel = exportBillOfLadingDto.getExporterTel();
        if(exportBillOfLadingDto.getExporterAddress()==null){this.exporterAddress=exportBillOfLadingDto.getConsignorAddress();}
        this.exporterAddress = exportBillOfLadingDto.getExporterAddress();
        if(exportBillOfLadingDto.getExporterTin()==null){this.exporterTin=exportBillOfLadingDto.getConsignorTin();}
        this.exporterTin = exportBillOfLadingDto.getExporterTin();
        this.consigneeName = exportBillOfLadingDto.getConsigneeName();
        this.consigneeTel = exportBillOfLadingDto.getConsigneeTel();
        this.consigneeAddress = exportBillOfLadingDto.getConsigneeAddress();
        this.consigneeTin = exportBillOfLadingDto.getConsigneeTin();
        this.notifyName = exportBillOfLadingDto.getNotifyName();
        this.notifyTel = exportBillOfLadingDto.getNotifyTel();
        this.notifyAddress = exportBillOfLadingDto.getNotifyAddress();
        this.notifyTin = exportBillOfLadingDto.getNotifyTin();
        this.lastUpdateId = "TESWS";
        this.firstRegisterId ="TESWS";
        this.blDescription=exportBillOfLadingDto.getBlDescription();
        this.packingType=exportBillOfLadingDto.getBlPackingType();
        this.placeOfDestination = exportBillOfLadingDto.getPlaceOfDestination();
        this.blGrossWeight =exportBillOfLadingDto.getBlSummary().getBlGrossWeight();
        this.blNetWeight =exportBillOfLadingDto.getBlSummary().getBlNetWeight();
        this.auditStatus="NA";
        this.containerCount=exportBillOfLadingDto.getBlSummary().getNumberOfContainers();

    }
}
