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
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@DynamicUpdate
@Table(name = "TANCISINT.CM_IF_TESWS_FREE_PRATIQUE")
public class FreePratiqueReportEntity implements Serializable {
    @Id
    @NotNull
    @Column(name = "TPA_UID")
    private String communicationAgreedId;

    @Column(name = "VESSEL_MASTER")
    private String vesselMaster;

    @Column(name = "VESSEL_MASTER_ADDRESS")
    private String vesselMasterAddress;

    @Column(name="AGENT_CD")
    private String agentCode;

    @Column(name="AGENT_ADDRESS")
    private String agentAddress;

    @Column(name="VOYAGE_NO")
    private String voyageNumber;

    @Column(name="CALL_SIGN")
    private String callSign;

    @Column(name="IMO_NO")
    private String transportMeansId;

    @Column(name="VESSEL_NAME")
    private String transportMeansName;

    @Column(name="NATIONALITY")
    private String transportMeansNationality;

    @Column(name="INSPECTION_VERDICT")
    private String inspectionVerdict;

    @Column(name="INSPECTION_DATE")
    private LocalDateTime inspectionDate;

    @Column(name="INSPECTION_LINK")
    private String reportLink;

    @Column(name="FIRST_REGISTER_DT", nullable = false, updatable = false)
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
