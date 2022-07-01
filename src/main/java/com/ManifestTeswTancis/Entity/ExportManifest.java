package com.ManifestTeswTancis.Entity;

import com.ManifestTeswTancis.Request.PortCallIdRequestModel;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@DynamicUpdate
@Table(name = "TANCISEXT.EX_MF_EXPORT_MANIFEST")
public class ExportManifest implements Serializable {
    @Id
    @Column(name="MRN",unique = true)
    private String mrnOut;

    @NotNull
	@Column(name ="CUSTOMS_OFFICE_CD")
    private String customOfficeCode;

	@Column(name="PROCESSING_STATUS")
    private String processingStatus;

	@Column(name ="PROCESSING_DT")
    private Date processingDate;

	@Column(name="PROCESSING_ID")
    private String processingId;

	@Column(name ="TRANSPORT_MODE")
    private String modeOfTransport;

	@Column(name ="CARRIER_CD")
    private String carrierId;

	@Column(name="CARRIER_NAME")
    private String carrierName;

	@Column(name="NATIONALITY")
    private String transportMeansNationality;

	@Column(name="IMO_NO")
    private String transportMeansId;

	@Column(name="CALL_SIGN")
    private String callSign;

	@Column(name="VESSEL_NAME")
    private String transportMeansName;

	@Column(name ="FLIGHT_REGISTRATION_NO")
    private String flightRegistrationNo;

	@Column(name="VOYAGE_NO")
    private String voyageNumberOutbound;

	@Column(name="FLIGHT_NO")
    private String flightNo;

	@Column(name="VEHICLE_NO")
    private String vehicleNo;

	@Column(name="CHASSIS_NO")
    private String chassisNo;

	@Column(name="LOADING_PORT")
    private String loadingPort;

	@Column(name ="DESTINATION_PORT")
    private String destinationPort;

	@Column(name="ACTUAL_DEPARTURE_DT")
    private LocalDateTime actualDatetimeOfDeparture;

	@Column(name="TERMINAL_CD")
    private String terminal;

	@Column(name="TERMINAL_OPERATOR_CD")
    private String terminalOperatorCode;

	@Column(name="AUDITOR")
    private String auditor;

	@Column(name ="MRN_ERROR_TYPE")
    private String mrnErrorType;

	@Column(name="MRN_ERROR_DT")
    private Date mrnErrorDt;

	@Column(name="MRN_ERROR_COMMENT")
    private String mrnErrorComment;

	@Column(name="BILL_YN")
    private String billYn;

	@Column(name="BILL_NO")
    private String billNo;

	@Column(name="BILL_ISSUE_DT")
    private Date billIssueDt;

	@Column(name="PAYMENT_DT")
    private Date paymentDt;

	@Column(name="TPA_UID")
    private String communicationAgreedId;

	@Column(name="SUBMIT_DT")
    private Date submitDt;

	@Column(name="TOTAL_MASTER_BL")
    private Integer totalMasterBl;

	@Column(name="DECLARANT_CD")
    private String declarantCd;

	@Column(name="DECLARANT_TIN")
    private String declarantTin;

	@Column(name="DECLARANT_NAME")
    private String declarantName;

	@Column(name="DECLARANT_TEL")
    private String declarantTel;

	@Column(name="FIRST_REGISTER_ID")
    private String firstRegisterId;

	@Column(name="FIRST_REGISTER_DT", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime firstRegisterDate;

	@Column(name="LAST_UPDATE_ID")
    private String lastUpdateId;

	@Column(name="LAST_UPDATE_DT")
    @UpdateTimestamp
    private LocalDateTime lastUpdateDate;

	@Column(name="EXPECTED_DEPARTURE_DT")
    private LocalDateTime estimatedDatetimeOfDeparture;

	@Column(name="IN_BALLAST_YN")
    private String ballast;

	@Column(name="SENT_TO_TESWS")
    private String sentToTesws;

	public ExportManifest (PortCallIdRequestModel callInfDetails){
	    this.terminalOperatorCode=callInfDetails.getTerminalOperatorCode();
	    this.callSign = callInfDetails.getCallSign();
	    this.carrierId=callInfDetails.getCarrierId();
	    this.carrierName=callInfDetails.getCarrierName();
	    this.communicationAgreedId=callInfDetails.getCommunicationAgreedId();
        this.customOfficeCode = callInfDetails.getCustomOfficeCode();
	    this.actualDatetimeOfDeparture= callInfDetails.getActualDatetimeOfDeparture();
	    this.destinationPort =callInfDetails.getDestinationPort();
	    this.voyageNumberOutbound=callInfDetails.getVoyageNumberOutbound();
	    this.transportMeansId=callInfDetails.getTransportMeansId();
	    this.processingStatus="1";
	    this.auditor="NA";
        this.submitDt= new Date();
        this.processingDate= new Date();
	    this.processingId="SYSTEM";
	    this.lastUpdateId="TESWS";
	    this.firstRegisterId="TESWS";
        this.transportMeansNationality=callInfDetails.getTransportMeansNationality();
        this.transportMeansName=callInfDetails.getTransportMeansName();
        this.terminal=callInfDetails.getTerminal();
        this.terminalOperatorCode=callInfDetails.getTerminalOperatorCode();
        this.modeOfTransport= callInfDetails.getModeOfTransport();
        this.estimatedDatetimeOfDeparture=callInfDetails.getEstimatedDatetimeOfDeparture();
        if(callInfDetails.isOutwardCargo()){this.ballast="N";}
        if(!callInfDetails.isOutwardCargo()){this.ballast="Y";}

    }

}

