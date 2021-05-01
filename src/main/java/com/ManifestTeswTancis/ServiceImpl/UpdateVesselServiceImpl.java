package com.ManifestTeswTancis.ServiceImpl;

import com.ManifestTeswTancis.Entity.ExImportManifest;
import com.ManifestTeswTancis.Repository.ExImportManifestRepository;
import com.ManifestTeswTancis.Service.UpdateVesselService;
import com.ManifestTeswTancis.dtos.TeswsResponse;
import com.ManifestTeswTancis.dtos.UpdateVesselDto;
import com.ManifestTeswTancis.Util.DateFormatter;
import com.ManifestTeswTancis.Util.HttpCall;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import com.ManifestTeswTancis.Util.HttpMessage;
import org.springframework.stereotype.Service;

@Service
public class UpdateVesselServiceImpl implements UpdateVesselService {
    final
    ExImportManifestRepository exImportManifestRepository;
    final
    ManifestStatusServiceImp manifestStatusServiceImp;

    public UpdateVesselServiceImpl(ExImportManifestRepository exImportManifestRepository, ManifestStatusServiceImp manifestStatusServiceImp) {
        this.exImportManifestRepository = exImportManifestRepository;
        this.manifestStatusServiceImp = manifestStatusServiceImp;
    }


    @Override
    public TeswsResponse updateCallInf(UpdateVesselDto updateVesselDto) {
        TeswsResponse response = new TeswsResponse();
        response.setAckDate(DateFormatter.getTeSWSLocalDate(LocalDateTime.now()));
        response.setRefId(updateVesselDto.getCommunicationAgreedId());
        response.setCode(200);
        response.setDescription("Vessel call Updates Received Successfully");
        response.setAckType("VESSEL_CALL_UPDATE");
try {
        Optional<ExImportManifest> optional = exImportManifestRepository
                .findFirstByCommunicationAgreedId(updateVesselDto.getCommunicationAgreedId());
        if(optional.isPresent()) {
            ExImportManifest ex = new ExImportManifest();
            ex.setCommunicationAgreedId(updateVesselDto.getCommunicationAgreedId());
            ex.setControlReferenceNumber(updateVesselDto.getControlReferenceNumber());
            ex.setTerminalOperatorCode(updateVesselDto.getTerminalOperatorCode());
            ex.setTerminal(updateVesselDto.getTerminal());
            ex.setVoyageNumber(updateVesselDto.getVoyageNumber());
            ex.setModeOfTransport(updateVesselDto.getModeOfTransport());
            ex.setCarrierId(updateVesselDto.getCarrierId());
            ex.setCarrierName(updateVesselDto.getCarrierName());
            ex.setTransportMeansId(updateVesselDto.getTransportMeansId());
            ex.setTransportMeansName(updateVesselDto.getTransportMeansName());
            ex.setCustomOfficeCode(updateVesselDto.getCustomOfficeCode());
            ex.setTransportMeansNationality(updateVesselDto.getTransportMeansNationality());
            ex.setCarWeightAtDestination(updateVesselDto.getCarWeightAtDestination());
            ex.setCarWeightAtDischarge(updateVesselDto.getCarWeightAtDischarge());
            ex.setCarWeightLoaded(updateVesselDto.getCarWeightLoaded());
            ex.setCarQuantityAtDestination(updateVesselDto.getCarQuantityAtDestination());
            ex.setCarQuantityAtDischarge(updateVesselDto.getCarQuantityAtDischarge());
            ex.setCarQuantityLoaded(updateVesselDto.getCarQuantityLoaded());
            ex.setBkWeightAtDestination(updateVesselDto.getBkWeightAtDestination());
            ex.setBkWeightAtDischarge(updateVesselDto.getBkWeightAtDischarge());
            ex.setBkWeightLoaded(updateVesselDto.getBkWeightLoaded());
            ex.setBkQuantityAtDestination(updateVesselDto.getBkQuantityAtDestination());
            ex.setBkQuantityAtDischarge(updateVesselDto.getBkQuantityAtDischarge());
            ex.setBkQuantityLoaded(updateVesselDto.getBkQuantityLoaded());
            ex.setCnWeightAtDestination(updateVesselDto.getCnWeightAtDestination());
            ex.setCnWeightAtDischarge(updateVesselDto.getCnWeightAtDischarge());
            ex.setCnWeightLoaded(updateVesselDto.getCnWeightLoaded());
            ex.setCnQuantityAtDestination(updateVesselDto.getCnQuantityAtDestination());
            ex.setCnQuantityAtDischarge(updateVesselDto.getCnQuantityAtDischarge());
            ex.setCnQuantityLoaded(updateVesselDto.getCnQuantityLoaded());
            ex.setNextPortOfCall(updateVesselDto.getNextPortOfCall());
            ex.setPortOfCall(updateVesselDto.getPortOfCall());
            ex.setDestinationPort(updateVesselDto.getDestinationPort());
            ex.setEstimatedDatetimeOfArrival(DateFormatter.getDateFromLocalDateTime(updateVesselDto.getEstimatedDatetimeOfArrival()));
            ex.setActualDateTimeOfArrival(DateFormatter.getDateFromLocalDateTime(updateVesselDto.getActualDatetimeOfArrival()));
            ex.setEstimatedDatetimeOfDeparture(DateFormatter.getDateFromLocalDateTime(updateVesselDto.getEstimatedDatetimeOfDeparture()));
            exImportManifestRepository.save(ex);
        }
} catch (Exception e) {
    response.setDescription(e.getMessage());
    response.setCode(400);
    System.err.println(e.getMessage());
}

        return response;
    }
    @Override
    public String submitCallInfoNotice(UpdateVesselDto updateVesselDto) throws IOException {
        HttpMessage httpMessage = new HttpMessage();
        httpMessage.setContentType("application/json");
        httpMessage.setMessageName("VESSEL_CALL_UPDATE");
        httpMessage.setRecipient("SS");
        HttpCall httpCall = new HttpCall();
        return httpCall.httpRequest(httpMessage);
    }

}
