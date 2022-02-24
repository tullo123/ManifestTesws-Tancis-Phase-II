package com.ManifestTeswTancis.Entity;
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
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name="TANCISINT.CM_CUSTOM_CLEARANCE")
public class CustomClearanceEntity implements Serializable {

    @Id
    @Column(name="TPA_UID")
    private String communicationAgreedId;

    @Column(name="PROCESSING_STATUS")
    private  String processingStatus;

    @Column(name="PROCESSING_DT")
    private  Date processingDate;

    @Column(name="VESSEL_MASTER")
    private String vesselMaster;

    @Column(name="VESSEL_MASTER_ADDRESS")
    private String vesselMasterAddress;

    @Column(name="AGENT_CD")
    private String agentCode;

    @Column(name="AGENT_ADDRESS")
    private String agentAddress;

    @Column(name="VOYAGE_NO")
    private String voyageNumber;

    @Column(name="CARRIER_ID")
    private String carrierId;

    @Column(name="CARRIER_NAME")
    private String carrierName;

    @Column(name="VESSEL_NAME")
    private String transportMeansName;

    @Column(name="NATIONALITY")
    private String transportMeansNationality;

    @Column(name="VOYAGE_OUT_BOUND")
    private String voyageNumberOutbound;

    @Column(name="TERMINAL")
    private String terminal;

    @Column(name="DESTINATION_PORT")
    private String destinationPort;

    @Column(name="NEXT_ARRIVAL_PORT_CD")
    private String portOfCall;

    @Column(name="NEXT_PORT_CALL")
    private String nextPortOfCall;

    @Column(name="TAX_CLEARANCE_NO")
    private String taxClearanceNumber;

    @Column(name="DEPARTURE_DT")
    private Date estimatedDatetimeOfDeparture;

    @Column(name="EST_DATE_ARRIVAL")
    private Date estimatedDatetimeOfArrival;

    @Column(name="ACT_DATE_ARRIVAL")
    private Date actualDatetimeOfArrival;

    @Column(name="CLEARANCE_REQUEST_DT")
    private Date clearanceRequestDate;

    @Column(name = "CREATED_DATE")
    @CreationTimestamp
    private LocalDateTime createdAt;

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

    @Column(name="COMMENTS")
    private  String comments;

    @Column(name = "LOADING_CONTAINER_COUNT")
    private Double cnQuantityLoaded;

    @Column(name = "LOADING_CONTAINER_WEIGHT")
    private Double cnWeightLoaded;

    @Column(name = "LOADING_BULK_COUNT")
    private Double bkQuantityLoaded;

    @Column(name = "LOADING_BULK_WEIGHT")
    private Double bkWeightLoaded;

    @Column(name = "LOADING_CAR_COUNT")
    private Double carQuantityLoaded;

    @Column(name = "LOADING_CAR_WEIGHT")
    private Double carWeightLoaded;

    @Column(name="AUDIT_DT")
    private  Date auditDate;
}

