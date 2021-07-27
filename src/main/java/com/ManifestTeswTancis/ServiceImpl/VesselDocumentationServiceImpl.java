package com.ManifestTeswTancis.ServiceImpl;

import com.ManifestTeswTancis.Entity.VesselDocumentationEntity;
import com.ManifestTeswTancis.Repository.VesselDocumentationRepository;
import com.ManifestTeswTancis.Service.VesselDocumentationService;
import com.ManifestTeswTancis.Util.DateFormatter;
import com.ManifestTeswTancis.dtos.DocumentDto;
import com.ManifestTeswTancis.dtos.TeswsResponse;
import com.ManifestTeswTancis.dtos.VesselDocumentation;
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
    public TeswsResponse vesselDocumentationReceiving(VesselDocumentation vesselDocumentation) {
        TeswsResponse response = new TeswsResponse();
        response.setAckType("VESSEL_DOCUMENTATIONS");
        response.setRefId(vesselDocumentation.getCommunicationAgreedId());
        response.setCode(200);
        response.setAckDate(DateFormatter.getTeSWSLocalDate(LocalDateTime.now()));
        response.setDescription("Vessel Documentation Received Successfully");

        try{
            Optional<VesselDocumentationEntity> optional=vesselDocumentationRepository.
                     findFirstByCommunicationAgreedId(vesselDocumentation.getCommunicationAgreedId());
            if(!optional.isPresent()) {
                 VesselDocumentationEntity vessel = new VesselDocumentationEntity();
                 vessel.setCommunicationAgreedId(vesselDocumentation.getCommunicationAgreedId());
                 vessel.setControlReferenceNumber(vesselDocumentation.getControlReferenceNumber());
                 vessel.setVesselMaster(vesselDocumentation.getVesselMaster());
                 vessel.setVesselMasterAddress(vesselDocumentation.getVesselMasterAddress());
                 vessel.setAgentCode(vesselDocumentation.getAgentCode());
                 vessel.setAgentAddress(vesselDocumentation.getAgentAddress());
                 vessel.setTerminalOperatorCode(vesselDocumentation.getTerminalOperatorCode());
                 vessel.setVoyageNumber(vesselDocumentation.getVoyageNumber());
                 vessel.setCarrierId(vesselDocumentation.getCarrierId());
                 vessel.setCarrierName(vesselDocumentation.getCarrierName());
                 vessel.setCallSign(vesselDocumentation.getCallSign());
                 vessel.setTransportMeansId(vesselDocumentation.getTransportMeansId());
                 vessel.setTransportMeansName(vesselDocumentation.getTransportMeansName());
                 vessel.setTransportMeansNationality(vesselDocumentation.getTransportMeansNationality());
                 vessel.setTerminal(vesselDocumentation.getTerminal());
                 vessel.setDestinationPort(vesselDocumentation.getDestinationPort());
                 vessel.setPortOfCall(vesselDocumentation.getPortOfCall());
                 vessel.setNextPortOfCall(vesselDocumentation.getNextPortOfCall());
                 vessel.setLastUpdateId("TESWS");
                 vessel.setFirstRegisterId("TESWS");

                for (DocumentDto documentDto : vesselDocumentation.getDocuments()) {
                    vessel.setDocumentName(documentDto.getDocumentName());
                    vessel.setDocumentLink(documentDto.getDocumentLink());
                }
                vesselDocumentationRepository.save(vessel);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        response.setDescription("Vessel Documentation with CommunicationAgreedId " + vesselDocumentation.getCommunicationAgreedId()
                + " is already present in Tancis");
        response.setCode(405);
        return response;

    }
}
