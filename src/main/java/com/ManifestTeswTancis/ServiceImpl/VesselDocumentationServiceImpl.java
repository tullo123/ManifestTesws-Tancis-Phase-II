package com.ManifestTeswTancis.ServiceImpl;

import com.ManifestTeswTancis.Entity.VesselDocumentationEntity;
import com.ManifestTeswTancis.Repository.VesselDocumentationRepository;
import com.ManifestTeswTancis.Service.VesselDocumentationService;
import com.ManifestTeswTancis.Util.DateFormatter;
import com.ManifestTeswTancis.dtos.DocumentDto;
import com.ManifestTeswTancis.dtos.TeswsResponse;
import com.ManifestTeswTancis.dtos.VesselDocumentationDto;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class VesselDocumentationServiceImpl implements VesselDocumentationService {

    final
    VesselDocumentationRepository vesselDocumentationRepository;

    public VesselDocumentationServiceImpl(VesselDocumentationRepository vesselDocumentationRepository) {
        this.vesselDocumentationRepository = vesselDocumentationRepository;
    }

    @Override
    public TeswsResponse vesselDocumentationReceiving(VesselDocumentationDto vesselDocumentationDto) {
        TeswsResponse response = new TeswsResponse();
        response.setAckType("VESSEL_DOCUMENTATIONS");
        response.setRefId(vesselDocumentationDto.getCommunicationAgreedId());
        response.setCode(200);
        response.setAckDate(DateFormatter.getTeSWSLocalDate(LocalDateTime.now()));
        response.setDescription("Vessel Documentation Received Successfully");

        try{
            Optional<VesselDocumentationEntity> optional=vesselDocumentationRepository.
                     findFirstByCommunicationAgreedId(vesselDocumentationDto.getCommunicationAgreedId());
            if(!optional.isPresent()) {
                 VesselDocumentationEntity vessel = new VesselDocumentationEntity();
                 vessel.setCommunicationAgreedId(vesselDocumentationDto.getCommunicationAgreedId());
                 vessel.setControlReferenceNumber(vesselDocumentationDto.getControlReferenceNumber());
                 vessel.setVesselMaster(vesselDocumentationDto.getVesselMaster());
                 vessel.setVesselMasterAddress(vesselDocumentationDto.getVesselMasterAddress());
                 vessel.setAgentCode(vesselDocumentationDto.getAgentCode());
                 vessel.setAgentAddress(vesselDocumentationDto.getAgentAddress());
                 vessel.setTerminalOperatorCode(vesselDocumentationDto.getTerminalOperatorCode());
                 vessel.setVoyageNumber(vesselDocumentationDto.getVoyageNumber());
                 vessel.setCarrierId(vesselDocumentationDto.getCarrierId());
                 vessel.setCarrierName(vesselDocumentationDto.getCarrierName());
                 vessel.setCallSign(vesselDocumentationDto.getCallSign());
                 vessel.setTransportMeansId(vesselDocumentationDto.getTransportMeansId());
                 vessel.setTransportMeansName(vesselDocumentationDto.getTransportMeansName());
                 vessel.setTransportMeansNationality(vesselDocumentationDto.getTransportMeansNationality());
                 vessel.setTerminal(vesselDocumentationDto.getTerminal());
                 vessel.setDestinationPort(vesselDocumentationDto.getDestinationPort());
                 vessel.setPortOfCall(vesselDocumentationDto.getPortOfCall());
                 vessel.setNextPortOfCall(vesselDocumentationDto.getNextPortOfCall());
                 vessel.setLastUpdateId("TESWS");
                 vessel.setFirstRegisterId("TESWS");

                for (DocumentDto documentDto : vesselDocumentationDto.getDocuments()) {
                    vessel.setDocumentName(documentDto.getDocumentName());
                    vessel.setDocumentLink(documentDto.getDocumentLink());
                }
                vesselDocumentationRepository.save(vessel);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        response.setDescription("Vessel Documentation with CommunicationAgreedId " + vesselDocumentationDto.getCommunicationAgreedId()
                + " is already present in Tancis");
        response.setCode(405);
        return response;

    }
}
