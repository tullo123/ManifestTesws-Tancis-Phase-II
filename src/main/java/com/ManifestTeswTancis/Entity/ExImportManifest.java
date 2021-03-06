package com.ManifestTeswTancis.Entity;
import com.ManifestTeswTancis.Request.PortCallIdRequestModel;
import com.ManifestTeswTancis.Util.DateFormatter;
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
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@DynamicUpdate //added 28-May-2021
@Table(name = "TANCISEXT.EX_MF_IMPORT_MANIFEST")
public class ExImportManifest implements Serializable {
	@Id
	@NotNull
	@Column(name = "MRN", unique = true)
	private String mrn;

	@NotNull
	@Column(name = "CUSTOMS_OFFICE_CD")
	private String customOfficeCode;

	@Column(name = "CONTROL_REFERENCE_NO")
	private String controlReferenceNumber;

	@Column(name = "TERMINAL_OPERATOR_CD")
	private String terminalOperatorCode;

	@Column(name = "TERMINAL_CD")
	private String terminal;

	@Column(name = "TPA_UID")
	private String communicationAgreedId;

	@Column(name = "MESSAGE_TYPE_CD")
	private String messageReferenceNumber;

	@Column(name = "IMO_NO")
	private String transportMeansId;

	@Column(name = "TRANSPORT_MODE")
	private String modeOfTransport;

	@Column(name = "VOYAGE_NO")
	private String voyageNumber;

	@Column(name = "CARRIER_CD")
	private String carrierId;

	@Column(name = "CARRIER_NAME")
	private String carrierName;

	@Column(name = "CALL_SIGN")
	private String callSign;

	@Column(name = "VESSEL_NAME")
	private String transportMeansName;

	@Column(name = "NEXT_ARRIVAL_PORT_CD")
	private String nextPortOfCall;

	@Column(name = "ACTUAL_ARRIVAL_DT")
	private Date actualDateTimeOfArrival;

	@Column(name = "DEPARTURE_DT")
	private Date actualDatetimeOfDeparture;

	@Column(name = "EXPECTED_DEPARTURE_DT")
	private Date estimatedDatetimeOfDeparture;

	@Column(name = "NATIONALITY")
	private String transportMeansNationality;

	@Column(name = "DISCHARGE_PORT")
	private String destinationPort;

	@Column(name = "DEPARTURE_PORT")
	private String portOfCall;

	@Column(name = "PROCESSING_STATUS")
	private String processingStatus;

	@Column(name = "FIRST_REGISTER_DT", nullable = false, updatable = false)
	@CreationTimestamp
	private LocalDateTime firstRegisterDate;

	@Column(name = "FIRST_REGISTER_ID")
	private String firstRegisterId;

	@Column(name = "LAST_UPDATE_ID")
	private String lastUpdateId;

	@Column(name = "LAST_UPDATE_DT")
	@UpdateTimestamp
	private LocalDateTime lastUpdateDate;

	@Column(name = "PROCESSING_DT")
	private Date processingDate;

	@Column(name = "PROCESSING_ID")
	private String processingId;

	@Column(name = "LOADING_CAR_COUNT")
	private Double carQuantityLoaded;

	@Column(name = "LOADING_CAR_WEIGHT")
	private Double carWeightLoaded;

	@Column(name = "LOADING_CONTAINER_COUNT")
	private Double cnQuantityLoaded;

	@Column(name = "LOADING_CONTAINER_WEIGHT")
	private Double cnWeightLoaded;

	@Column(name = "DISCHARGE_CAR_COUNT")
	private Integer carQuantityAtDischarge;

	@Column(name = "DISCHARGE_CAR_WEIGHT")
	private Double carWeightAtDischarge;

	@Column(name = "DISCHARGE_CONTAINER_COUNT")
	private Double cnQuantityAtDischarge;

	@Column(name = "DISCHARGE_CONTAINER_WEIGHT")
	private Double cnWeightAtDischarge;

	@Column(name = "DESTINATION_CAR_COUNT")
	private Integer carQuantityAtDestination;

	@Column(name = "DESTINATION_CAR_WEIGHT")
	private Double carWeightAtDestination;

	@Column(name = "DESTINATION_CONTAINER_COUNT")
	private Double cnQuantityAtDestination;

	@Column(name = "DESTINATION_CONTAINER_WEIGHT")
	private Double cnWeightAtDestination;

	@Column(name = "LOADING_BULK_COUNT")
	private Double bkQuantityLoaded;

	@Column(name = "LOADING_BULK_WEIGHT")
	private Double bkWeightLoaded;

	@Column(name = "DISCHARGE_BULK_COUNT")
	private Double bkQuantityAtDischarge;

	@Column(name = "DISCHARGE_BULK_WEIGHT")
	private Double bkWeightAtDischarge;

	@Column(name = "DESTINATION_BULK_COUNT")
	private Double bkQuantityAtDestination;

	@Column(name = "DESTINATION_BULK_WEIGHT")
	private Double bkWeightAtDestination;

	@Column(name = "EXPECTED_ARRIVAL_DT")
	private Date estimatedDatetimeOfArrival;

	@Column(name = "IN_BALLAST_YN")
	private String ballast;

	@Column(name="BOARDING_YN")
	private String boardingYn;

	@Column(name="BOARDING_DT")
	private LocalDateTime boardingDt;

	@Column(name="SUBMIT_DT")
	private Date submitDt;

	public ExImportManifest(PortCallIdRequestModel callInfDetails) {
		this.customOfficeCode = callInfDetails.getCustomOfficeCode();
		this.controlReferenceNumber = callInfDetails.getControlReferenceNumber();
		this.terminalOperatorCode = callInfDetails.getTerminalOperatorCode();
		this.terminal = callInfDetails.getTerminal();
		this.communicationAgreedId = callInfDetails.getCommunicationAgreedId();
		this.transportMeansId = callInfDetails.getTransportMeansId();
		this.modeOfTransport = callInfDetails.getModeOfTransport();
		this.voyageNumber = callInfDetails.getVoyageNumber();
		this.carrierId = callInfDetails.getCarrierId();
		this.carrierName = callInfDetails.getCarrierName();
		this.transportMeansName = callInfDetails.getTransportMeansName();
		this.callSign=callInfDetails.getCallSign();
		this.nextPortOfCall = callInfDetails.getNextPortOfCall();
		this.actualDateTimeOfArrival = DateFormatter.getDateFromLocalDateTime(callInfDetails.getEstimatedDatetimeOfArrival());
		if(callInfDetails.getEstimatedDatetimeOfDeparture()!=null)
		{this.actualDatetimeOfDeparture = DateFormatter.getDateFromLocalDateTime(callInfDetails.getEstimatedDatetimeOfDeparture());}
		else{this.actualDatetimeOfDeparture = DateFormatter.getDateFromLocalDateTime(callInfDetails.getEstimatedDatetimeOfArrival());}
		this.processingDate= new Date();
		this.transportMeansNationality = callInfDetails.getTransportMeansNationality();
		this.destinationPort = callInfDetails.getDestinationPort();
		this.submitDt= new Date();
		this.portOfCall = callInfDetails.getPortOfCall();
		this.processingStatus = "1";
		this.firstRegisterId = "TESWS";
		this.processingId = "SYSTEM";
		this.lastUpdateId ="TESWS";
		this.messageReferenceNumber=callInfDetails.getMessageTypeId();
		this.carQuantityLoaded = callInfDetails.getCarQuantityLoaded();
		this.carWeightLoaded = callInfDetails.getCarWeightLoaded();
		this.cnQuantityLoaded = callInfDetails.getCnQuantityLoaded();
		this.cnWeightLoaded = callInfDetails.getCnWeightLoaded();
		this.carQuantityAtDischarge = callInfDetails.getCarQuantityAtDischarge();
		this.carWeightAtDischarge = callInfDetails.getCarWeightAtDischarge();
		this.cnQuantityAtDischarge = callInfDetails.getCnQuantityAtDischarge();
		this.cnWeightAtDischarge = callInfDetails.getCnWeightAtDischarge();
		this.carQuantityAtDestination = callInfDetails.getCarQuantityAtDestination();
		this.carWeightAtDestination = callInfDetails.getCarWeightAtDestination();
		this.cnQuantityAtDestination = callInfDetails.getCnQuantityAtDestination();
		this.cnWeightAtDestination = callInfDetails.getCnWeightAtDestination();
		this.bkQuantityLoaded = callInfDetails.getBkQuantityLoaded();
		this.bkWeightLoaded = callInfDetails.getBkWeightLoaded();
		this.bkQuantityAtDischarge = callInfDetails.getBkQuantityAtDischarge();
		this.bkWeightAtDischarge = callInfDetails.getBkWeightAtDischarge();
		this.bkQuantityAtDestination = callInfDetails.getBkQuantityAtDestination();
		this.bkWeightAtDestination = callInfDetails.getBkWeightAtDestination();
		this.estimatedDatetimeOfArrival = DateFormatter.getDateFromLocalDateTime(callInfDetails.getEstimatedDatetimeOfArrival());
		this.estimatedDatetimeOfDeparture=DateFormatter.getDateFromLocalDateTime(callInfDetails.getActualDatetimeOfDeparture());
		if(callInfDetails.getBallast().equalsIgnoreCase("true")){this.ballast="Y";}
		if(callInfDetails.getBallast().equalsIgnoreCase("false")){this.ballast="N";}
	}

}
