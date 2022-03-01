package com.ManifestTeswTancis.Entity;

import com.ManifestTeswTancis.dtos.BillOfLadingDto;
import com.ManifestTeswTancis.idEntities.MasterBlId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@DynamicUpdate
@Table(name = "TANCISEXT.EX_MF_IMPORT_MASTER_BL")
@IdClass(MasterBlId.class)
public class ExImportMasterBl {

	@Id
	@Column(name = "MASTER_BL_NO")
	private String masterBillOfLading;

	@Id
	@Column(name = "MRN")
	private String mrn;

	@Column(name = "PACKAGE_UNIT")
	private String packageUnit;

	@Column(name = "MSN")
	private String msn;

	@Column(name = "TASAC_CONTROL_NUMBER")
	private String tasacControlNumber;

	@Column(name = "BL_PACKAGE")
	private Double blPackage;

	@Column(name = "CARGO_CLASSIFICATION")
	private String tradeType;

	@Column(name = "SHIPPING_AGENT_CD")
	private String shippingAgentCode;

	@Column(name = "SHIPPING_MARKS")
	private String marksNumbers;

	@Column(name = "FORWARDER_CD")
	private String forwarderCode;

	@Column(name = "FORWARDER_NAME")
	private String forwarderName;

	@Column(name = "FORWARDER_TEL")
	private String forwarderTel;

	@Column(name = "EXPORTER_NAME")
	private String exporterName;

	@Column(name = "EXPORTER_TEL")
	private String exporterTel;

	@Column(name = "EXPORTER_ADDRESS")
	private String exporterAddress;

	@Column(name = "EXPORTER_TIN")
	private String exporterTin;

	@Column(name = "CONSIGNEE_NAME")
	private String consigneeName;

	@Column(name = "CONSIGNEE_TEL")
	private String consigneeTel;

	@Column(name = "CONSIGNEE_ADDRESS")
	private String consigneeAddress;

	@Column(name = "CONSIGNEE_TIN")
	private String consigneeTin;

	@Column(name = "NOTIFY_NAME")
	private String notifyName;

	@Column(name = "NOTIFY_TEL")
	private String notifyTel;

	@Column(name = "NOTIFY_ADDRESS")
	private String notifyAddress;

	@Column(name = "NOTIFY_TIN ")
	private String notifyTin;

	@Column(name = "GOODS_DESCRIPTION")
	private String blDescription;

	@Column(name = "IMDG_CD")
	private String imdgclass;

	@Column(name = "BL_GROSS_WEIGHT")
	private Double blGrossWeight;

	@Column(name = "GROSS_WEIGHT_UNIT")
	private String grossWeightUnit;

	@Column(name = "BL_NET_WEIGHT")
	private Double blNetWeight;

	@Column(name = "NET_WEIGHT_UNIT")
	private String netWeightUnit;

	@Column(name = "BL_GROSS_VOLUME")
	private Double volume;

	@Column(name = "VOLUME_UNIT")
	private String volumeUnit;

	@Column(name = "PACKING_TYPE")
	private String blPackingType;

	@Column(name = "OIL_TYPE")
	private String oilType;

	@Column(name = "LOADING_PORT")
	private String portOfLoading;

	@Column(name = "DESTINATION_PLACE")
	private String placeOfDestination;

	@Column(name = "DELIVERY_PLACE")
	private String placeOfDelivery;

	@Column(name = "AUDIT_STATUS")
	private String auditStatus;

	@Column(name = "AUDITOR")
	private String auditor;

	@Column(name = "AUDIT_DT")
	private Date auditDate;

	@Column(name = "AUDIT_COMMENT")
	private String auditComment;

	@Column(name = "INVOICE_VALUE")
	private Double invoiceValue;

	@Column(name = "INVOICE_CURRENCY")
	private String invoiceCurrency;

	@Column(name = "FREIGHT_CHARGE")
	private Double freightCharge;

	@Column(name = "FREIGHT_CURRENCY")
	private String freightCurrency;

	@Column(name = "BL_TYPE")
	private String blType;

	@Column(name="CONSOLIDATED_STATUS")
	private String consolidatedStatus;

	@Column(name="FIRST_REGISTER_DT")
	@CreationTimestamp
	private Date firstRegisterDate;

	@Column(name = "FIRST_REGISTER_ID")
	private String firstRegisterId;

	@Column(name = "LAST_UPDATE_DT")
	@UpdateTimestamp
	private Date lastUpdateDate;

	@Column(name = "LAST_UPDATE_ID")
	private String lastUpdateId;

	@Column(name = "CONTAINER_COUNT")
	private Integer containerCount;


	public ExImportMasterBl(BillOfLadingDto billOfLadingDto) {
		this.masterBillOfLading = billOfLadingDto.getMasterBillOfLading();
		this.tasacControlNumber = billOfLadingDto.getTasacControlNumber();
		this.tradeType = billOfLadingDto.getTradeType();
		this.firstRegisterId="TESWS";
		this.lastUpdateId="TESWS";
		this.shippingAgentCode = billOfLadingDto.getShippingAgentCode();
		this.forwarderCode = billOfLadingDto.getForwarderCode();
		this.forwarderName = billOfLadingDto.getForwarderName();
		this.forwarderTel = billOfLadingDto.getForwarderTel();
		this.exporterName = billOfLadingDto.getExporterName();
		this.exporterTel = billOfLadingDto.getExporterTel();
		this.exporterAddress = billOfLadingDto.getExporterAddress();
		this.exporterTin = billOfLadingDto.getExporterTin();
		this.consigneeName = billOfLadingDto.getConsigneeName();
		this.consigneeTel = billOfLadingDto.getConsigneeTel();
		this.consigneeAddress = billOfLadingDto.getConsigneeAddress();
		this.consigneeTin = billOfLadingDto.getConsigneeTin();
		this.notifyName = billOfLadingDto.getNotifyName();
		this.notifyTel = billOfLadingDto.getNotifyTel();
		this.notifyAddress = billOfLadingDto.getNotifyAddress();
		this.notifyTin = billOfLadingDto.getNotifyTin();
		this.portOfLoading = billOfLadingDto.getPortOfLoading();
		this.placeOfDestination = billOfLadingDto.getPlaceOfDestination();
		this.placeOfDelivery = billOfLadingDto.getPlaceOfDelivery();
		this.invoiceValue = billOfLadingDto.getInvoiceValue();
		this.invoiceCurrency = billOfLadingDto.getInvoiceCurrency();
		this.freightCharge = billOfLadingDto.getFreightCharge();
		this.freightCurrency = billOfLadingDto.getFreightCurrency();
		this.blType = (billOfLadingDto.getHouseBillOfLading() != null)?"C":"S";
		this.consolidatedStatus = (billOfLadingDto.getHouseBillOfLading() != null)?"Y":"N";
		this.marksNumbers =billOfLadingDto.getMarksNumbers();
		this.blGrossWeight=billOfLadingDto.getBlSummary().getBlGrossWeight();
		this.blNetWeight= billOfLadingDto.getBlSummary().getBlNetWeight();
	}

}
