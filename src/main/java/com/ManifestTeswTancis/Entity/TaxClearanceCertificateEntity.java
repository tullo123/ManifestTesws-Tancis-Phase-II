package com.ManifestTeswTancis.Entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "ETAC.TAX_CLEARANCE_CERTIFICATE")
public class TaxClearanceCertificateEntity implements Serializable {
    @Id
    @NotNull
    @Column(name="CERTIFICATENUMBER")
    private Integer certificateNumber;

    @NotNull
    @Column(name="TAXPAYER_ID")
    private Integer taxpayerId;

    @NotNull
    @Column(name="TAXYEAR")
    private Integer taxYear;

    @NotNull
    @Column(name="TAXOFFICE")
    private Integer taxOffice;

    @NotNull
    @Column(name="CERTIFICATETYPE")
    private String certificateType;

    @NotNull
    @Column(name="NATIONALITY")
    private String nationality;

    @NotNull
    @Column(name="STATUS")
    private String status;

    @Column(name="REASONS")
    private String reasons;

    @Column(name="AMOUNTPAID")
    private Double amountPaid;

    @NotNull
    @Column(name="DATERECEIVED")
    private Date dateReceived;

    @Column(name="EXPIRYDATE")
    private Date expiryDate;

    @Column(name="TAXCERTIFICATENUMBER")
    private String taxCertificateNumber;

    @Column(name="NUMBERBUSINESSACTIVITIES")
    private Integer numberBusinessActivities;

    @Column(name="BUSINESSACTIVITIES")
    private String businessActivities;

    @Column(name="TELEPHONE")
    private String telephone;

    @Column(name="EMAIL")
    private String email;

    @Column(name="DATEPRINTED")
    private Date datePrinted;

    @Column(name="REPRINTREASON")
    private String reprintReason;

    @Column(name="DATENOTIFICATIONSENT")
    private Date dateNotificationSent;

    @Column(name="DATECERTIFICATEDISPATCHED")
    private Date dateCertificateDispatched;

    @Column(name="APPROVEDDATE")
    private Date approvedDate;

    @Column(name="APPROVEDBY")
    private String approvedBy;

    @NotNull
    @Column(name="ENTRYDATE")
    private Date entryDate;

    @NotNull
    @Column(name="STAFFNO")
    private String staffNumber;

    @Column(name="PLACEOFBUSINESSPLOTNO")
    private String placeOfBusinessPlotNumber;

    @Column(name="PLACEOFBUSINESSBLOCK")
    private String placeOfBusinessBlock;

    @Column(name="STREET_LOCATION")
    private String streetLocation;

    @Column(name="LICENCINGTIN")
    private Integer licencingTin;

    @Column(name="ADDRESS1")
    private String addressOne;

    @Column(name="ADDRESS2")
    private String addressTwo;

    @Column(name="ADDRESS3")
    private String addressThree;

    @Column(name="TRAOFFICERRECOMMENDATIONS")
    private String traOfficerRecommendations;

    @Column(name="LICENCINGNAME")
    private String licencingName;

    @Column(name="REMARK1")
    private String remarkOne;

    @Column(name="REMARK2")
    private String remarkTwo;

    @Column(name="REMARK3")
    private String remarkThree;

    @NotNull
    @Column(name="BRANCH_ID")
    private Integer blanchId;

    @Column(name="PLOTNUMBER")
    private String plotNumber;

    @Column(name="BLOCKNUMBER")
    private String blockNumber;

    @Column(name="STREET")
    private String Street;

    @Column(name="REGIONCITY_CT")
    private String regionalCityCt;

    @Column(name="DISTRICTCITY_CT")
    private String districtCityCt;

    @Column(name="CERT_OPTION")
    private Integer certOption;

    @Column(name="DEBITNO")
    private Integer debitNo;

    @Column(name="TITLENUMBER")
    private String titleNumber;

    @Column(name="TAXPAYER_NAME")
    private String taxpayerName;
}
