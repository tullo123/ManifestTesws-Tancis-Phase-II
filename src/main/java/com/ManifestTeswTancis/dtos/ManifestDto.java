package com.ManifestTeswTancis.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ManifestDto {
	private Date preparationDateTime;
	@NotNull(message = "Communication agreed id  can not be NULL.")
	private String communicationAgreedId;
	private String controlReferenceNumber;
	private String applicationReference;
	private String messageReferenceNumber;
	private String manifestReferenceNumber;
	private String messageTypeId;
	private String messageFunction;
	private Date cargoReportDate;
	private String transportStageType;
	private String voyageNumber;
	private String modeOfTransportCoded;
	private String modeOfTransport;
	private String carrierId;
	private String carrierName;
	private String transportMeansId;
	private String transportMeansName;
	private String transportMeansNationality;
	private String shippingLineId;
	private String shippingLineName;
	private String customOfficeCode;
	private String destinationPort;
	private String portOfCall;
	private String nextPortOfCall;
	private String terminal;
	private String terminalOperator;
	private ManifestSummary manifestSummary;
	private Date estimatedDatetimeOfDeparture;
	private Date estimatedDatetimeOfArrival;
	private List<BillOfLadingDto> consignments= new ArrayList<>();
	private List<ContainerDto> containers =new ArrayList<>();

}
