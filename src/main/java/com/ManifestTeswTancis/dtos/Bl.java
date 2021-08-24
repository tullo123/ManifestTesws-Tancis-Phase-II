package com.ManifestTeswTancis.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Bl implements Serializable {
    private String action;
    private String blId;
    private String masterBillOfLading;
    private String houseBillOfLading;
    private String blType;
    private String blPackingType;
    private String blDescription;
    private BlSummary blSummary;
    private String tradeType;
    private String portOfOrigin;
    private String portOfDischarge;
    private String placeOfDestination;
    private String placeOfDelivery;
    private String portOfLoading;
    private String crn;
    private String tasacControlNumber;
    private String shippingAgentCode;
    private String shippingAgentName;
    private String shippingLineCode;
    private String shippingLineName;
    private String forwarderCode;
    private String forwarderName;
    private String forwarderContactName;
    private String forwarderLocationCode;
    private String forwarderLocationName;
    private String forwarderTel;
    private String exporterName;
    private String exporterTel;
    private String exporterAddress;
    private String exporterLocationCode;
    private String exporterLocationName;
    private String exporterContactName;
    private String exporterTin;
    private String consigneeName;
    private String consigneeTel;
    private String consigneeAddress;
    private String consigneeLocationCode;
    private String consigneeLocationName;
    private String consigneeContactName;
    private String consigneeTin;
    private String consignorName;
    private String consignorTel;
    private String consignorAddress;
    private String consignorLocationCode;
    private String consignorLocationName;
    private String consignorContactName;
    private String consignorTin;
    private String notifyName;
    private String notifyTel;
    private String notifyAddress;
    private String notifyLocationCode;
    private String notifyLocationName;
    private String notifyContactName;
    private String notifyTin;
    private String notifyTwoName;
    private String notifyTwoTel;
    private String notifyTwoAddress;
    private String notifyTwoLocationCode;
    private String notifyTwoLocationName;
    private String notifyTwoContactName;
    private String notifyTwoTin;
    private List<GoodDetails> goodDetails = new ArrayList<>();
}
