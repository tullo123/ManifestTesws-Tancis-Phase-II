package com.ManifestTeswTancis.Entity;

import com.ManifestTeswTancis.idEntities.ManifestAmendStagingTblId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@DynamicUpdate
@Table(name = "TTANCISINT.CM_MF_AMENDMENT_STG_TBL")
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
    private Date firstRegisterDate;

    @Column(name="FIRST_REGISTER_ID")
    private String firstRegisterId;

    @Column(name="LAST_UPDATE_DT")
    private Date lastUpdateDate;

    @Column(name="LAST_UPDATE_ID")
    private String lastUpdateId;
}
