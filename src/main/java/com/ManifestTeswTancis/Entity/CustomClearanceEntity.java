package com.ManifestTeswTancis.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

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
@Table(name="TANCISINT.CM_CUSTOM_CLEARANCE")
public class CustomClearanceEntity implements Serializable {

    @Id
    @Column(name="TPA_UID")
    private String communicationAgreedId;

    @Column(name="PROCESSING_STATUS")
    private  String processingStatus;

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

    @Column(name="MRN")
    private String mrn;

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
    private String estimatedDatetimeOfDeparture;

    @Column(name="EST_DATE_ARRIVAL")
    private String estimatedDatetimeOfArrival;

    @Column(name="ACT_DATE_ARRIVAL")
    private String actualDatetimeOfArrival;

    @Column(name="CLEARANCE_REQUEST_DT")
    private String clearanceRequestDate;

    @Column(name="CREATED_DATE")
    @CreationTimestamp
    private LocalDateTime createDate;

    @Column(name="COMMENTS")
    private  String comments;


}

