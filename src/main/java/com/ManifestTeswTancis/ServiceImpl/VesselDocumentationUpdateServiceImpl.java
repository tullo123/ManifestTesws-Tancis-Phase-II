package com.ManifestTeswTancis.ServiceImpl;

import com.ManifestTeswTancis.Entity.VesselDocumentationEntity;
import com.ManifestTeswTancis.Repository.VesselDocumentationRepository;
import com.ManifestTeswTancis.Service.VesselDocumentationUpdateService;
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
public class VesselDocumentationUpdateServiceImpl implements VesselDocumentationUpdateService {
    final VesselDocumentationRepository vesselDocumentationRepository;

    public VesselDocumentationUpdateServiceImpl(VesselDocumentationRepository vesselDocumentationRepository) {
        this.vesselDocumentationRepository = vesselDocumentationRepository;
    }

    @Override
    public TeswsResponse vesselDocumentationUpdate(VesselDocumentationDto vesselDocumentationDto) {
        TeswsResponse response = new TeswsResponse();
        response.setAckDate(DateFormatter.getTeSWSLocalDate(LocalDateTime.now()));
        response.setRefId(vesselDocumentationDto.getCommunicationAgreedId());
        response.setDescription("Vessel Documentation Update Received Successfully");
        response.setAckType("VESSEL_DOCUMENTATIONS_UPDATE");

        try{
            Optional<VesselDocumentationEntity> optional=vesselDocumentationRepository.
                    findFirstByCommunicationAgreedId(vesselDocumentationDto.getCommunicationAgreedId());
            if(optional.isPresent()){
                VesselDocumentationEntity vesselDocument= optional.get();
                vesselDocument.setControlReferenceNumber(vesselDocumentationDto.getControlReferenceNumber());
                vesselDocument.setVesselMaster(vesselDocumentationDto.getVesselMaster());
                vesselDocument.setVesselMasterAddress(vesselDocumentationDto.getVesselMasterAddress());
                vesselDocument.setAgentCode(vesselDocumentationDto.getAgentCode());
                vesselDocument.setAgentAddress(vesselDocumentationDto.getAgentAddress());
                vesselDocument.setTerminalOperatorCode(vesselDocumentationDto.getTerminalOperatorCode());
                vesselDocument.setVoyageNumber(vesselDocumentationDto.getVoyageNumber());
                vesselDocument.setCarrierId(vesselDocumentationDto.getCarrierId());
                vesselDocument.setCarrierName(vesselDocumentationDto.getCarrierName());
                vesselDocument.setCallSign(vesselDocumentationDto.getCallSign());
                vesselDocument.setTransportMeansId(vesselDocumentationDto.getTransportMeansId());
                vesselDocument.setTransportMeansName(vesselDocumentationDto.getTransportMeansName());
                vesselDocument.setTransportMeansNationality(vesselDocumentationDto.getTransportMeansNationality());
                vesselDocument.setTerminal(vesselDocumentationDto.getTerminal());
                vesselDocument.setDestinationPort(vesselDocumentationDto.getDestinationPort());

                for(DocumentDto documentDto: vesselDocumentationDto.getDocuments()){
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

