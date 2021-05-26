package com.ManifestTeswTancis.Entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "TANCISINT.CM_CL_VESSEL_DEPARTURE_NOTICE")
public class VesselDepartureNoticeEntity implements Serializable {
	private static final long serialVersionUID = 5342811567846227138L;

	@Id
	@Column(name = "TPA_UID ")
	private String communicationAgreedId;

	@Column(name = "CONTROL_REF_NO")
	private String controlReferenceNumber;

	@Column(name="MESSAGE_TYPE_ID")
	private String messageTypeId;

	@Column(name="MESSAGE_FUNCTION")
	private String messageFunction;

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

	@Column(name="TRANSPORT_MODE_CD")
	private String modeOfTransportCoded;

	@Column(name="TRANSPORT_MODE")
	private String modeOfTransport;

	@Column(name="CARRIER_CD")
	private String carrierId;

	@Column(name="IMO_NO")
	private String transportMeansId;

	@Column(name="VESSEL_NAME")
	private String transportMeansName;

	@Column(name="NATIONALITY")
	private String transportMeansNationality;

	@Column(name="ROTATION_NO")
	private String rotationNumber;

	@Column(name="NEXT_PORT_CALL")
	private String nextPortOfCall;

	@Column(name="ACT_DEPARTURE_DT")
	private String actualDatetimeOfDeparture;

	@Column(name="HANDOVER_DT")
	private String handoverDatetime;

	@Column(name="ACT_DEPARTURE_ANCHORAGE_DT")
	private String actualDatetimeOfDepartureOuterAnchorage;

	@Column(name ="CREATED_AT" )
	@CreationTimestamp
	private LocalDateTime createdAt;

}
