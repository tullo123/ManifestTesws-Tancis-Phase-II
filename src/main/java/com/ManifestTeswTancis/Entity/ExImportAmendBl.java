package com.ManifestTeswTancis.Entity;

import com.ManifestTeswTancis.idEntities.BlAmendmentId;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "TANCISEXT.EX_MF_IMPORT_AMEND_BL")
@IdClass(BlAmendmentId.class)
public class ExImportAmendBl implements Serializable {
	private static final long serialVersionUID = -4366566369834189788L;

	@Id
	@Column(name = "DECLARANT_TIN")
	private String declatantTin;

	@NotNull
	@Column(name = "AMEND_YEAR")
	private String amendYear;

	@NotNull
	@Column(name = "PROCESS_TYPE")
	private String processType;

	@NotNull
	@Column(name = "AMEND_SERIAL_NO")
	private String amendSerialNumber;

	@NotNull
	@Column(name = "BL_NO")
	private String billOfLading;

	@Column(name = "PACKAGE_UNIT")
	private String packageType;

	@Column(name = "BL_PACKAGE")
	private Double blPackage;

//	@Column(name = "PACKAGE_UNIT" )
//	private String PackageUnit;

	@Column(name = "CARGO_CLASSIFICATION")
	private String tradeType;

	@Column(name = "SHIPPING_AGENT_CD")
	private String shippingAgentCode;

	@Column(name = "TASAC_CONTROL_NUMBER")
	private String tasacControlNumber;

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

	@Column(name = "NOTIFY_TIN ") // field in entity must match to fields in DB
	private String notifyTin;

	@Column(name = "GOODS_DESCRIPTION")
	private String description;

	@Column(name = "IMDG_CD")
	private String imdgclass;

	@Column(name = "BL_GROSS_WEIGHT")
	private Double grossWeight;

	@Column(name = "GROSS_WEIGHT_UNIT")
	private String grossWeightUnit;

	@Column(name = "BL_NET_WEIGHT")
	private Double netWeight;

	@Column(name = "NET_WEIGHT_UNIT")
	private String netWeightUnit;

	@Column(name = "BL_GROSS_VOLUME")
	private Double volume;

	@Column(name = "VOLUME_UNIT")
	private String volumeUnit;

	@Column(name = "PACKING_TYPE")
	private String packingType;

	@Column(name = "OIL_TYPE")
	private String oilType;

	@Column(name = "LOADING_PORT")
	private String portOfLoading;

	@Column(name = "DESTINATION_PLACE")
	private String placeOfDestination;

	@Column(name = "DELIVERY_PLACE")
	private String placeOfDelivery;

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

	@Column(name = "SHIPPING_MARKS")
	private String shippingMarks;

	@Column(name = "FIRST_REGISTER_ID")
	private String firstRegisterId;

	@Column(name = "FIRST_REGISTER_DT")
	private Date firstRegisterDate;

	@Column(name = "LAST_UPDATE_ID")
	private String lastUpdateId;
	
	@Column(name="LAST_UPDATE_DT")              
	private Date lastUpdateDate;

	@Column(name="DECL_YN")           
	private String declarationYesNo;

	@Column(name="REF_DECL_NO")                  
	private String referenceDeclarationNumber;

	@Column(name="FIRST_DESTINATION_PLACE")
	private String firstDestinationPlace;



}
