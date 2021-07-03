package com.ManifestTeswTancis.Entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name ="TANCISINT.CM_MF_IMPORT_GOODS_ITEMS")
public class BlGoodItemsEntity {
    @Id
    @NotNull
    @Column(name = "MRN")
    private String mrn;

    @NotNull
    @Column(name = "MASTER_BL_NO")
    private String masterBillOfLading;

    @Column(name = "HOUSE_BL_NO")
    private String houseBillOfLading;

    @Column(name = "GOOD_ITEM_NO")
    private Integer goodsItemNo;

    @Column(name = "GOOD_DESCRIPTIONS")
    private String description;

    @Column(name = "GOOD_PACKING_TYPE")
    private String packingType;

    @Column(name = "GOOD_PACKAGE_QUANTITY")
    private Double packageQuantity;

    @Column(name = "GOOD_PACKAGE_TYPE")
    private String packageType;

    @Column(name = "GOOD_OIL_TYPE")
    private String oilType;

    @Column(name = "GOOD_INVOICE_VALUE")
    private Double invoiceValue;

    @Column(name = "GOOD_INVOICE_CURRENCY")
    private String invoiceCurrency;

    @Column(name = "GOOD_FREIGHT_CHARGE")
    private Double freightCharge;

    @Column(name = "GOOD_FREIGHT_CURRENCY")
    private String freightCurrency;

    @Column(name ="GOOD_GROSS_WEIGHT")
    private Double grossWeight;

    @Column(name ="GOOD_GROSS_WEIGHT_UNIT")
    private String grossWeightUnit;

    @Column(name ="GOOD_NET_WEIGHT")
    private Double netWeight;

    @Column(name ="GOOD_NET_WEIGHT_UNIT")
    private String netWeightUnit;

    @Column(name ="GOOD_VOLUME")
    private Double volume;

    @Column(name ="GOOD_VOLUME_UNIT")
    private String volumeUnit;

    @Column(name ="GOOD_LENGTH")
    private Double length;

    @Column(name ="GOOD_LENGTH_UNIT")
    private String lengthUnit;

    @Column(name ="GOOD_WIDTH")
    private Double width;

    @Column(name ="GOOD_WIDTH_UNIT")
    private String widthUnit;

    @Column(name ="GOOD_HEIGHT")
    private Double height;

    @Column(name ="GOOD_HEIGHT_UNIT")
    private String heightUnit;

    @Column(name ="MARKS_NUMBERS")
    private String marksNumbers;

    @Column(name ="VEHICLE_VN")
    private String vehicleVIN;

    @Column(name ="VEHICLE_MODEL")
    private String vehicleModel;

    @Column(name ="VEHICLE_MAKE")
    private String vehicleMake;

    @Column(name ="VEHICLE_OWN_DRIVE")
    private String vehicleOwnDrive;

    @Column(name ="DGOOD_CLASS_CD")
    private String classCode;

    @Column(name ="FLASH_POINT_VALUE")
    private String flashpointValue;

    @Column(name ="SHIPFLASH_POINT_VALUE")
    private Double shipmFlashptValue;

    @Column(name ="SHIPFLASH_POINT_UNIT")
    private String shipmFlashptUnit;

    @Column(name ="PACKING_GROUP")
    private String packingGroup;

    @Column(name ="MARPOL_CATEGORY")
    private String marPolCategory;

    @Column(name ="IMDG_PAGE")
    private String imdgpage;

    @Column(name ="IMDG_CLASS")
    private String imdgclass;

    @Column(name ="UN_NUMBER")
    private String unnumber;

    @Column(name ="TREM_CARD")
    private String tremcard;

    @Column(name ="MFAG")
    private String mfag;

    @Column(name ="EMS")
    private String ems;

    @Column(name = "CONTAINER_NO")
    private String containerNo;

    @Column(name="FIRST_REGISTER_DT")
    @CreationTimestamp
    private LocalDateTime firstRegisterDate;

    @Column(name = "FIRST_REGISTER_ID")
    private String firstRegisterId;

    @Column(name = "LAST_UPDATE_DT")
    @UpdateTimestamp
    private LocalDateTime lastUpdateDate;

    @Column(name = "LAST_UPDATE_ID")
    private String lastUpdateId;

    }
