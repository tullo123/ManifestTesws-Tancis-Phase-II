package com.ManifestTeswTancis.Entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@DynamicUpdate
@Table(name = "TANCISINT.CUSTOM_APPROVAL_STATUS")
public class CustomClearanceApprovalStatus {
    @Id
    @NotNull
    @Column(name ="TPA_UID", unique = true)
    private String communicationAgreedId;

    @Column(name ="TAX_CLEARANCE_NO" )
    private String taxClearanceNumber;

	@Column(name ="VOYAGE_NO")
    private String voyageNumber;

	@Column(name ="APPROVED_STATUS" )
    private boolean approvedStatus;

	@Column(name ="FIRST_REGISTER_DT",nullable = false, updatable = false )
    @CreationTimestamp
    private LocalDateTime firstRegisterDate;

	@Column(name ="LAST_UPDATE_DT" )
    @UpdateTimestamp
    private LocalDateTime lastUpdateDate;

	@Column(name="RECEIVED_NOTICE_SENT")
    private boolean receivedNoticeSent;

	@Column(name="RECEIVED_NOTICE_DT")
    private String noticeDate;

	@Column(name="RECEIVED_FAILED_STATUS")
    private String receivedFailedStatus;

	@Column(name="REJECTED_YN")
    private String rejectedYn;
}
