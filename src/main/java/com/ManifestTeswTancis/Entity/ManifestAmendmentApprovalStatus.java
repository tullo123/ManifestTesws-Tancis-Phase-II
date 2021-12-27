package com.ManifestTeswTancis.Entity;

import com.ManifestTeswTancis.Util.DateFormatter;
import com.ManifestTeswTancis.dtos.ManifestAmendmentDto;
import com.ManifestTeswTancis.idEntities.ManifestAmendStagingTblId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@DynamicUpdate
@Table(name = "TANCISINT.CM_MF_AMENDMENT_STG_TBL")
@IdClass(ManifestAmendStagingTblId.class)
public class ManifestAmendmentApprovalStatus {
    @Id
    @Column(name="MRN")
    private String mrn;

    @Id
    @Column(name="TPA_UID")
    private String communicationAgreedId;

    @Id
    @Column(name="AMEND_REFERENCE")
    private String amendReference;

    @Column(name="AMEND_DATE")
    private String amendDate;

    @Column(name="VOYAGE_NO")
    private String voyageNumber;

    @Column(name="APPROVED_STATUS")
    private boolean approvedStatus;

    @Column(name="RECEIVED_NOTICE_SENT")
    private boolean receivedNoticeSent;

    @Column(name="RECEIVED_NOTICE_DT")
    private String receivedNoticeDate;

    @Column(name="FIRST_REGISTER_DT")
    @CreationTimestamp
    private Date firstRegisterDate;

    @Column(name="FIRST_REGISTER_ID")
    private String firstRegisterId;

    @Column(name="LAST_UPDATE_DT")
    @UpdateTimestamp
    private Date lastUpdateDate;

    @Column(name="LAST_UPDATE_ID")
    private String lastUpdateId;

    @Column(name="AMEND_TYPE")
    private String amendType;

    @Column(name="AMEND_SERIAL_NO")
    private String amendSerialNo;

    public ManifestAmendmentApprovalStatus(ManifestAmendmentDto manifestAmendmentDto){
        this.mrn=manifestAmendmentDto.getMrn();
        this.communicationAgreedId=manifestAmendmentDto.getCommunicationAgreedId();
        this.amendReference=manifestAmendmentDto.getAmendmentReference();
        this.amendDate=manifestAmendmentDto.getAmendDate();
        this.voyageNumber=manifestAmendmentDto.getVoyageNumber();
        this.receivedNoticeDate= DateFormatter.getTeSWSLocalDate(LocalDateTime.now());
        this.firstRegisterId="TESWS";
        this.lastUpdateId="TESWS";
    }
}
