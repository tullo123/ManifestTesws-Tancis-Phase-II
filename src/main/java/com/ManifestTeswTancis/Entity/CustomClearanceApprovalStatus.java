package com.ManifestTeswTancis.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

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
@Table(name = "TANCISINT.CUSTOM_APPROVAL_STATUS")
public class CustomClearanceApprovalStatus {
    @Id
    @Column(name ="TPA_UID")
    private String communicationAgreedId;

    @Column(name ="MRN" )
    private String mrn;

	@Column(name ="VOYAGE_NO")
    private String voyageNumber;

	@Column(name ="APPROVED_STATUS" )
    private boolean approvedStatus;

	@Column(name ="CREATED_AT" )
    @CreationTimestamp
    private LocalDateTime createdAt;

	@Column(name ="UPDATED_AT" )
    @CreationTimestamp
    private LocalDateTime updatedAt;
}
