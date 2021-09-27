package com.ManifestTeswTancis.Entity;

import com.ManifestTeswTancis.dtos.BillOfLadingDto;
import com.ManifestTeswTancis.idEntities.HouseBlId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "TANCISEXT.EX_MF_IMPORT_HOUSE_BL")
@IdClass(HouseBlId.class)
public class ExImportHouseBl {

	@Id
	@Column(name = "HOUSE_BL_NO")
	private String houseBillOfLading;

	@Id
	@Column(name = "MASTER_BL_NO")
	private String masterBillOfLading;

	@Id
	@Column(name = "MRN")
	private String mrn;

	@Column(name = "MSN")
	private String msn;

	@Column(name = "HSN")
	private String hsn;

	@Column(name = "TASAC_CONTROL_NUMBER")
	private String tasacControlNumber;

	@Column(name = "PACKAGE_UNIT")
	private String packageUnit;

	@Column(name = "BL_PACKAGE")
	private Double blPackage;

	@Column(name = "CARGO_CLASSIFICATION")
	private String tradeType;

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
	private String description;

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

	@Column(name = "FIRST_REGISTER_DT")
	@CreationTimestamp
	private LocalDateTime firstRegisterDate;

	@Column(name = "FIRST_REGISTER_ID")
	private String firstRegisterId;

	@Column(name = "LAST_UPDATE_ID")
	private String lastUpdateId;

	@Column(name = "LAST_UPDATE_DT")
	@UpdateTimestamp
	private LocalDateTime lastUpdateDate;

	@Column(name = "INVOICE_VALUE")
	private Double invoiceValue;

	@Column(name = "INVOICE_CURRENCY")
	private String invoiceCurrency;

	@Column(name = "FREIGHT_CHARGE")
	private Double freightCharge;

	@Column(name = "FREIGHT_CURRENCY")
	private String freightCurrency;

	@Column(name = "DESTINATION_PLACE")
	private String placeOfDestination;


	public ExImportHouseBl(BillOfLadingDto billOfLadingDto) {
		this.houseBillOfLading = billOfLadingDto.getHouseBillOfLading();
		this.masterBillOfLading = billOfLadingDto.getMasterBillOfLading();
		this.tasacControlNumber = billOfLadingDto.getTasacControlNumber();
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
		this.lastUpdateId = "TESWS";
		this.firstRegisterId ="TESWS";
		this.description=billOfLadingDto.getBlDescription();
		this.blPackingType=billOfLadingDto.getBlPackingType();
		this.invoiceValue =billOfLadingDto.getInvoiceValue();
		this.invoiceCurrency=billOfLadingDto.getInvoiceCurrency();
		this.freightCharge=billOfLadingDto.getFreightCharge();
		this.freightCurrency=billOfLadingDto.getFreightCurrency();
		this.placeOfDestination = billOfLadingDto.getPlaceOfDestination();
		this.blGrossWeight =billOfLadingDto.getBlSummary().getBlGrossWeight();
		this.blNetWeight =billOfLadingDto.getBlSummary().getBlNetWeight();
	}

}
