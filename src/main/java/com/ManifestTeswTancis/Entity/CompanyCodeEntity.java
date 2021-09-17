package com.ManifestTeswTancis.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
@Table(name="TANCISEXT.CO_COMPANY_CODE")
public class CompanyCodeEntity {

    @Id
   @Column(name="COMPANY_CD")
   private String companyCode;

   @Column(name="TIN")
   private String tin;

   @Column(name="COMPANY_TYPE")
    private String companyType;

    @Column(name="COMPANY_NAME")
    private String companyName;

    @Column(name="REGISTRATION_CUSTOMS_OFFICE_CD")
    private String registrationCustomsOfficeCd;

    @Column(name="REPRESENTATIVE_NAME")
    private String representativeName;

    @Column(name="TEL_NO")
    private String telNo;

    @Column(name="FAX_NO")
    private String faxNo;

    @Column(name="ADDRESS")
    private String address;

    @Column(name="USE_YN")
    private String useYn;

    @Column(name="FIRST_REGISTER_ID")
    private String firstRegisterId;

    @Column(name="FIRST_REGISTER_DT")
    private LocalDateTime firstRegisterDate;

    @Column(name="LAST_UPDATE_ID")
    private String lastUpdateId;

    @Column(name="LAST_UPDATE_DT")
    private LocalDateTime lastUpdateDate;

    @Column(name="COMPANY_OPTION")
    private String companyOption;

    @Column(name="PRIVATE_YN")
    private String privateYn;

    @Column(name="STORAGE_PERIOD")
    private Integer storagePeriod;

    @Column(name="STORAGE_PERIOD_UNIT")
    private Integer storagePeriodUnit;

    @Column(name="REFERENCE_CD")
    private Integer referenceCd;
}
