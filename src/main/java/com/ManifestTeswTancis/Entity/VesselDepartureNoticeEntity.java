package com.ManifestTeswTancis.Entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "TANCISEXT.EX_CL_VESSEL_DEPARTURE_NOTICE")
public class VesselDepartureNoticeEntity implements Serializable {
	private static final long serialVersionUID = 5342811567846227138L;

	@Id
	@Column(name = "TPA_UID  ")
	private String communicationAgreedId;

	@Column(name = "VESSEL_MASTER")
	private String vesselMaster;

	@Column(name = "VESSEL_MASTER_ADDRESS")
	private String vesselMasterAddress;

	@Column(name = "AGENT_CD")
	private String agentCode;

	@Column(name = "AGENT_ADDRESS")
	private String agentAddress;

	@Column(name = "VOYAGE_NO")
	private String voyageNumber;

	@Column(name = "TRANSPORT_MODE")
	private String modeOfTransport;

	@Column(name = "CARRIER_CD")
	private String carrierId;

	@Column(name = "CARRIER_NAME")
	private String carrierName;

	@Column(name = "IMO_NO ")
	private String transportMeansId;

	@Column(name = "MEANS_OF_TRANSPORT")
	private String transportMeansName;

	@Column(name = "NATIONALITY ")
	private String transportMeansNationality;

	@Column(name = "VOYAGE_NO_OUTBOUND")
	private String voyageNumberOutbound;

	@Column(name = "TERMINAL_OPERATOR_CD")
	private String terminalOperatorCode;

	@Column(name = "TERMINAL_CD")
	private String terminal;

	@Column(name = "DESTINATION_PORT")
	private String destinationPort;

	@Column(name = "DEPARTURE_PORT")
	private String portOfCall;

	@Column(name = "NEXT_ARRIVAL_PORT_CD")
	private String nextPortOfCall;

	@Column(name = "EXPECTED_ARRIVAL_DT")
	private String estimatedDatetimeOfArrival;

	@Column(name = "EXPECTED_DEPARTURE_DT")
	private String estimatedDatetimeOfDeparture;

	@Column(name = "ACTUAL_ARRIVAL_ANCHORAGE")
	private String actualDatetimeOfArrivalOuterAnchorage;

	@Column(name = "HANDOVER_DATE_TIME")
	private String handoverDatetime;

	@Column(name = "ACTUAL_ARRIVAL_DT")
	private String actualDatetimeOfArrival;

	@Column(name = "DEPARTURE_DT")
	private String actualDatetimeOfDeparture;

	@Column(name = "ACTUAL_DEPARTURE_ANCHORAGE")
	private String actualDatetimeOfDepartureOuterAnchorage;


	@Column(name="CONTROL_REFERENCE_NO")
	private String controlReferenceNumber;
	
	@Column(name="MESSAGE_TYPE_ID")
	private String messageTypeId;
	
	@Column(name="MESSAGE_FUNCTION")
	private String messageFunction;
	
	
    @Column(name="TRANSPORT_STAGE_TYPE")
	private String transportStageType;
    
    @Column(name="TRANSPORT_MODE_CD")
	private String modeOfTransportCoded;
    
    
    @Column(name="ROTATION_NO")
	private String rotationNumber;
    
    @Column(name="BERTH_NO")
	private String berthNo;
    
    @Column(name="QUANTITY")
	private Integer quantity;
    
    @Column(name="QUANTITY_UNIT")
	private String quantityUnit;
    
    @Column(name="QUANTITY_OUTBOUND")
	private Integer quantityOutbound;
    
    @Column(name="QUANTITY_OUTBOUND_UNIT")
	private String quantityUnitOutbound;
    
    @Column(name="DRAFT_FORE")
	private Integer draftFore;
    
    @Column(name="DRAFT_FORE_UNIT")
	private String draftForeUnit;
    
    @Column(name="DRAFT_AFTER")
	private Integer draftAfter;
    
    @Column(name="DRAFT_AFTER_UNIT")
	private String draftAfterUnit;

}
