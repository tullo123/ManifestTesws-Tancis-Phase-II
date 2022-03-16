package com.ManifestTeswTancis.Entity;

import com.ManifestTeswTancis.dtos.ExportBillOfLadingDto;
import com.ManifestTeswTancis.idEntities.ExportMasterBlId;
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
@Table(name = "TANCISEXT.EX_MF_EXPORT_MASTER_BL")
@IdClass(ExportMasterBlId.class)
public class ExportMasterBlEntity implements Serializable {
    @Id
    @Column(name="MRN")
    private String mrn;

    @Id
    @Column(name="MASTER_BL_NO")
    private String masterBillOfLading;

	@Column(name="MSN")
    private String msn;

	@Column(name="CARGO_CLASSIFICATION")
    private String tradeType;

	@Column(name="BL_TYPE")
    private String blType;

	@Column(name="SHIPPING_AGENT_CD")
    private String shippingAgentCode;

	@Column(name="FORWARDER_CD")
    private String forwarderCode;

	@Column(name="FORWARDER_NAME")
    private String forwarderName;

	@Column(name="FORWARDER_TEL")
    private String forwarderTel;

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

	@Column(name="DISCHARGE_PORT")
    private Integer portOfDischarge;

	@Column(name="DESTINATION_PLACE")
    private String placeOfDestination;

	@Column(name="DELIVERY_PLACE")
    private String placeOfDelivery;

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
    private String billNumber;

	@Column(name="BILL_ISSUE_DT")
    private Date billIssueDate;

	@Column(name="PAYMENT_DT")
    private Date billPaymentDate;

	@Column(name="SHIPPING_MARKS")
    private String marksNumbers;

	@Column(name="AUDIT_STATUS")
    private String auditStatus;

	@Column(name="AUDITOR")
    private String auditor;

	@Column(name="AUDIT_DT")
    private Date auditDate;

	@Column(name="AUDIT_COMMENT")
    private String auditComment;

	@Column(name="HOUSE_BL_COUNT")
    private Integer houseBlCount;

	@Column(name="CONTAINER_COUNT")
    private Integer containerCount;

	@Column(name="CONSOLIDATED_STATUS")
    private String consolidatedStatus;

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


	public ExportMasterBlEntity(ExportBillOfLadingDto exportBillOfLadingDto){
        this.masterBillOfLading = exportBillOfLadingDto.getMasterBillOfLading();
        this.tradeType = exportBillOfLadingDto.getTradeType();
        this.firstRegisterId="TESWS";
        this.lastUpdateId="TESWS";
        this.auditStatus="NA";
        this.shippingAgentCode = exportBillOfLadingDto.getShippingAgentCode();
        this.forwarderCode = exportBillOfLadingDto.getForwarderCode();
        this.forwarderName = exportBillOfLadingDto.getForwarderName();
        this.forwarderTel = exportBillOfLadingDto.getForwarderTel();
        this.exporterName = exportBillOfLadingDto.getExporterName();
        this.exporterTel = exportBillOfLadingDto.getExporterTel();
        this.exporterAddress = exportBillOfLadingDto.getExporterAddress();
        this.exporterTin = exportBillOfLadingDto.getExporterTin();
        this.consigneeName = exportBillOfLadingDto.getConsigneeName();
        this.consigneeTel = exportBillOfLadingDto.getConsigneeTel();
        this.consigneeAddress = exportBillOfLadingDto.getConsigneeAddress();
        this.consigneeTin = exportBillOfLadingDto.getConsigneeTin();
        this.notifyName = exportBillOfLadingDto.getNotifyName();
        this.notifyTel = exportBillOfLadingDto.getNotifyTel();
        this.notifyAddress = exportBillOfLadingDto.getNotifyAddress();
        this.notifyTin = exportBillOfLadingDto.getNotifyTin();
        this.placeOfDestination = exportBillOfLadingDto.getPlaceOfDestination();
        this.placeOfDelivery = exportBillOfLadingDto.getPlaceOfDelivery();
        this.blGrossWeight=exportBillOfLadingDto.getBlSummary().getBlGrossWeight();
        this.blNetWeight= exportBillOfLadingDto.getBlSummary().getBlNetWeight();
        this.containerCount=exportBillOfLadingDto.getBlSummary().getNumberOfContainers();
        this.houseBlCount=exportBillOfLadingDto.getBlSummary().getNumberOfHouseBl();
    }

}
