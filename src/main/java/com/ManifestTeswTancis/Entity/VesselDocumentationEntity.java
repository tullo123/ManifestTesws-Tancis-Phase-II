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
@Table(name="TANCISINT.CM_IF_TESWS_VESSEL_DOC")
public class VesselDocumentationEntity implements Serializable {
    @Id
    @NotNull
    @Column(name="TPA_UID")
    private String communicationAgreedId;

    @Column(name="CONTROL_REF_NO")
    private String controlReferenceNumber;

    @Column(name="VESSEL_MASTER")
    private String vesselMaster;

    @Column(name="VESSEL_MASTER_ADDRESS")
    private String vesselMasterAddress;

    @Column(name="AGENT_CD")
    private String agentCode;

    @Column(name="AGENT_ADDRESS")
    private String agentAddress;

    @Column(name="TERMINAL_OPERATOR_CD")
    private String terminalOperatorCode;

    @Column(name="VOYAGE_NO")
    private String voyageNumber;

    @Column(name="CARRIER_CD")
    private String carrierId;

    @Column(name="CARRIER_NAME")
    private String carrierName;

    @Column(name="CALL_SIGN")
    private String callSign;

    @Column(name="IMO_NO")
    private String transportMeansId;

    @Column(name="VESSEL_NAME")
    private String transportMeansName;

    @Column(name="NATIONALITY")
    private String transportMeansNationality;

    @Column(name="TERMINAL")
    private String terminal;

    @Column(name="DESTINATION_PORT")
    private String destinationPort;

    @Column(name="PORT_CALL")
    private String portOfCall;

    @Column(name="NEXT_PORT_CALL")
    private String nextPortOfCall;

    @Column(name="DOCUMENT_NAME")
    private String documentName;

    @Column(name="DOCUMENT_LINK")
    private String documentLink;

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
