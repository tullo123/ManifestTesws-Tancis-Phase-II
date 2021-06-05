package com.ManifestTeswTancis.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@DynamicUpdate
@Table(name = "TANCISINT.CM_MF_IMPORT_MANIFEST")
public class InImportManifest implements Serializable {
    @Id
    @NotNull
    @Column(name = "MRN", unique = true)
    private String mrn;

    @NotNull
    @Column(name = "CUSTOMS_OFFICE_CD")
    private String customOfficeCode;

    @Column(name = "TERMINAL_OPERATOR_CD")
    private String terminalOperatorCode;

    @Column(name = "TERMINAL_CD")
    private String terminal;

    @NotNull
    @Column(name = "TPA_UID")
    private String communicationAgreedId;

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

    @Column(name = "FIRST_REGISTER_DT")
    private Date mrnDate;

    @Column(name = "NEXT_ARRIVAL_PORT_CD")
    private String nextPortOfCall;

    @Column(name = "ACTUAL_ARRIVAL_DT")
    private Date actualDateTimeOfArrival;

    @Column(name = "DEPARTURE_DT")
    private Date actualDatetimeOfDeparture;

    @Column(name = "NATIONALITY")
    private String transportMeansNationality;

    @Column(name = "DISCHARGE_PORT")
    private String destinationPort;

    @Column(name = "DEPARTURE_PORT")
    private String portOfCall;

    @Column(name = "PROCESSING_STATUS")
    private String processingStatus;

    @Column(name = "FIRST_REGISTER_DT", insertable = false, updatable = false)
    private Date firstRegisterDate;

    @Column(name = "FIRST_REGISTER_ID")
    private String firstRegisterId;

    @Column(name = "LAST_UPDATE_DT")
    private Date lastUpdateDate;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(name = "PROCESSING_DT", insertable = false, updatable = false)
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
    private Double carQuantityAtDischarge;

    @Column(name = "DISCHARGE_CAR_WEIGHT")
    private Double carWeightAtDischarge;

    @Column(name = "DISCHARGE_CONTAINER_COUNT")
    private Double cnQuantityAtDischarge;

    @Column(name = "DISCHARGE_CONTAINER_WEIGHT")
    private Double cnWeightAtDischarge;

    @Column(name = "DESTINATION_CAR_COUNT")
    private Double carQuantityAtDestination;

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
}
