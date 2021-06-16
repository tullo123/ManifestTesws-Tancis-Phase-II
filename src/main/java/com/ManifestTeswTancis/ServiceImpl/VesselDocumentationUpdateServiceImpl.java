package com.ManifestTeswTancis.ServiceImpl;

import com.ManifestTeswTancis.Entity.VesselDocumentationEntity;
import com.ManifestTeswTancis.Repository.VesselDocumentationRepository;
import com.ManifestTeswTancis.Service.VesselDocumentationUpdateService;
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
public class VesselDocumentationUpdateServiceImpl implements VesselDocumentationUpdateService {

    final
    VesselDocumentationRepository vesselDocumentationRepository;

    public VesselDocumentationUpdateServiceImpl(VesselDocumentationRepository vesselDocumentationRepository) {
        this.vesselDocumentationRepository = vesselDocumentationRepository;
    }

    @Override
    public TeswsResponse vesselDocumentationUpdate(VesselDocumentation vesselDocumentation) {
        TeswsResponse response = new TeswsResponse();
        response.setAckDate(DateFormatter.getTeSWSLocalDate(LocalDateTime.now()));
        response.setRefId(vesselDocumentation.getCommunicationAgreedId());
        response.setDescription("Vessel Documentation Update Received Successfully");
        response.setAckType("VESSEL_DOCUMENTATIONS_UPDATE");

        try{
            Optional<VesselDocumentationEntity> optional=vesselDocumentationRepository.
                    findFirstByCommunicationAgreedId(vesselDocumentation.getCommunicationAgreedId());
            if(optional.isPresent()){
                VesselDocumentationEntity vesselDocument= optional.get();
                vesselDocument.setControlReferenceNumber(vesselDocumentation.getControlReferenceNumber());
                vesselDocument.setVesselMaster(vesselDocumentation.getVesselMaster());
                vesselDocument.setVesselMasterAddress(vesselDocumentation.getVesselMasterAddress());
                vesselDocument.setAgentCode(vesselDocumentation.getAgentCode());
                vesselDocument.setAgentAddress(vesselDocumentation.getAgentAddress());
                vesselDocument.setTerminalOperatorCode(vesselDocumentation.getTerminalOperatorCode());
                vesselDocument.setVoyageNumber(vesselDocumentation.getVoyageNumber());
                vesselDocument.setCarrierId(vesselDocumentation.getCarrierId());
                vesselDocument.setCarrierName(vesselDocumentation.getCarrierName());
                vesselDocument.setCallSign(vesselDocumentation.getCallSign());
                vesselDocument.setTransportMeansId(vesselDocumentation.getTransportMeansId());
                vesselDocument.setTransportMeansName(vesselDocumentation.getTransportMeansName());
                vesselDocument.setTransportMeansNationality(vesselDocumentation.getTransportMeansNationality());
                vesselDocument.setTerminal(vesselDocumentation.getTerminal());
                vesselDocument.setDestinationPort(vesselDocumentation.getDestinationPort());

                for(DocumentDto documentDto:vesselDocumentation.getDocuments()){
                    vesselDocument.setDocumentName(documentDto.getDocumentName());
                    vesselDocument.setDocumentLink(documentDto.getDocumentLink());
                }
                vesselDocumentationRepository.save(vesselDocument);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
    }

