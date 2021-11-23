package com.ManifestTeswTancis.ServiceImpl;

import com.ManifestTeswTancis.Entity.FreePratiqueReportEntity;
import com.ManifestTeswTancis.Repository.FreePartiqueRepository;
import com.ManifestTeswTancis.Service.FreePartiqueService;
import com.ManifestTeswTancis.Util.DateFormatter;
import com.ManifestTeswTancis.dtos.FreePratiqueReportDto;
import com.ManifestTeswTancis.dtos.TeswsResponse;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class FreePartiqueServiceImpl implements FreePartiqueService {
    final
    FreePartiqueRepository freePartiqueRepository;

    public FreePartiqueServiceImpl(FreePartiqueRepository freePartiqueRepository) {
        this.freePartiqueRepository = freePartiqueRepository;
    }

    @Override
    public TeswsResponse freePartique(FreePratiqueReportDto freePratiqueReportDto) {
        TeswsResponse response = new TeswsResponse();
        response.setAckDate(DateFormatter.getTeSWSLocalDate(LocalDateTime.now()));
        response.setRefId(freePratiqueReportDto.getCommunicationAgreedId());
        response.setDescription("Free Partique Report Received");
        response.setAckType("FREE_PRATIQUE_REPORT");

        try {
            Optional<FreePratiqueReportEntity> optional=freePartiqueRepository.
                    findFirstByCommunicationAgreedId(freePratiqueReportDto.getCommunicationAgreedId());
            if(!optional.isPresent()) {
                FreePratiqueReportEntity freePartique = new FreePratiqueReportEntity();
                freePartique.setCommunicationAgreedId(freePratiqueReportDto.getCommunicationAgreedId());
                freePartique.setVesselMaster(freePratiqueReportDto.getVesselMaster());
                freePartique.setVesselMasterAddress(freePratiqueReportDto.getVesselMasterAddress());
                freePartique.setAgentCode(freePratiqueReportDto.getAgentCode());
                freePartique.setAgentAddress(freePratiqueReportDto.getAgentAddress());
                freePartique.setVoyageNumber(freePratiqueReportDto.getVoyageNumber());
                freePartique.setCallSign(freePratiqueReportDto.getCallSign());
                freePartique.setTransportMeansId(freePratiqueReportDto.getTransportMeansId());
                freePartique.setTransportMeansName(freePratiqueReportDto.getTransportMeansName());
                freePartique.setTransportMeansNationality(freePratiqueReportDto.getTransportMeansNationality());
                freePartique.setInspectionVerdict(freePratiqueReportDto.getInspectionVerdict());
                freePartique.setInspectionDate(freePratiqueReportDto.getInspectionDate());
                freePartique.setReportLink(freePratiqueReportDto.getReportLink());
                freePartique.setFirstRegisterId("TESWS");
                freePartique.setLastUpdateId("TESWS");
                freePartiqueRepository.save(freePartique);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.setDescription("Free partique Report with CommunicationAgreedId " + freePratiqueReportDto.getCommunicationAgreedId()
                + " is already present in Tancis");
        response.setCode(405);
        return response;
    }
}
