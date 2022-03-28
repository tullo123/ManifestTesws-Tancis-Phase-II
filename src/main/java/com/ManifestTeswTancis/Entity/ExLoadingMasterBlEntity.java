package com.ManifestTeswTancis.Entity;

import com.ManifestTeswTancis.idEntities.ExLoadingMasterBlId;
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
@Table(name = "TANCISEXT.EX_DL_LOADING_MASTER_BL")
@IdClass(ExLoadingMasterBlId.class)
public class ExLoadingMasterBlEntity implements Serializable {
    @Id
    @Column(name="DECLARANT_TIN")
    private String declarantTin;

    @Column(name="DECLARANT_YEAR")
    private String declarantYear;

	@Column(name="PROCESS_TYPE")
    private String processType;

	@Column(name="DECLARANT_SERIAL_NO")
    private String declarantSerialNo;

	@Column(name="MASTER_BL_NO")
    private String masterBillOfLading;

	@Column(name="CARGO_CLASSIFICATION")
    private String tradeType;

	@Column(name="BL_TYPE")
    private String blType;

	@Column(name="SHIPPING_AGENT_CD")
    private String shippingAgentCd;

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
    private String goodsDescription;

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
    private Double blGrossVolume;

	@Column(name="VOLUME_UNIT")
    private String volumeUnit;

	@Column(name="DISCHARGE_PORT")
    private String portOfDischarge;

	@Column(name="DESTINATION_PORT")
    private String destinationPort;

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

	@Column(name="SHIPPING_MARKS")
    private String marksNumbers;

	@Column(name="TERMINAL_EXPECTED_CARRY_IN_DT")
    private Date terminalExpectedCarryInDate;

	@Column(name="HOUSE_BL_COUNT")
    private Integer houseBlCount;

	@Column(name="CONTAINER_COUNT")
    private Integer containerCount;

	@Column(name="CONSOLIDATED_STATUS")
    private String consolidatedStatus;

	@Column(name="AUDIT_STATUS")
    private String auditStatus;

	@Column(name="AUDITOR")
    private String auditor;

	@Column(name="AUDIT_DT")
    private Date auditDate;

	@Column(name="AUDIT_COMMENT")
    private String auditComment;

	@Column(name="MANIFEST_SUBMIT_YN")
    private String manifestSubmitYn;

	@Column(name="BEFORE_DECLARATION_NO")
    private String beforeDeclarationNo;

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

	@Column(name="EXTERNAL_YN")
    private String externalYn;

	@Column(name="ORIGIN_PORT")
    private String originPort;
}
