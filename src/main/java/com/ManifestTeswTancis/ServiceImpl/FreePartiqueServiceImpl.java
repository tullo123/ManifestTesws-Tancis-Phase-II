package com.ManifestTeswTancis.ServiceImpl;

import com.ManifestTeswTancis.Entity.FreePratiqueReportEntity;
import com.ManifestTeswTancis.Repository.FreePartiqueRepository;
import com.ManifestTeswTancis.Service.FreePartiqueService;
import com.ManifestTeswTancis.Util.DateFormatter;
import com.ManifestTeswTancis.dtos.FreePratiqueReport;
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
    public TeswsResponse freePartique(FreePratiqueReport freePratiqueReport) {
        TeswsResponse response = new TeswsResponse();
        response.setAckDate(DateFormatter.getTeSWSLocalDate(LocalDateTime.now()));
        response.setRefId(freePratiqueReport.getCommunicationAgreedId());
        response.setDescription("Free Partique Report Received");
        response.setAckType("FREE_PRATIQUE_REPORT");

        try {
            Optional<FreePratiqueReportEntity> optional=freePartiqueRepository.
                    findFirstByCommunicationAgreedId(freePratiqueReport.getCommunicationAgreedId());
            if(!optional.isPresent()) {
                FreePratiqueReportEntity freePartique = new FreePratiqueReportEntity();
                freePartique.setCommunicationAgreedId(freePratiqueReport.getCommunicationAgreedId());
                freePartique.setVesselMaster(freePratiqueReport.getVesselMaster());
                freePartique.setVesselMasterAddress(freePratiqueReport.getVesselMasterAddress());
                freePartique.setAgentCode(freePratiqueReport.getAgentCode());
                freePartique.setAgentAddress(freePratiqueReport.getAgentAddress());
                freePartique.setVoyageNumber(freePratiqueReport.getVoyageNumber());
                freePartique.setCallSign(freePratiqueReport.getCallSign());
                freePartique.setTransportMeansId(freePratiqueReport.getTransportMeansId());
                freePartique.setTransportMeansName(freePratiqueReport.getTransportMeansName());
                freePartique.setTransportMeansNationality(freePratiqueReport.getTransportMeansNationality());
                freePartique.setInspectionVerdict(freePratiqueReport.getInspectionVerdict());
                freePartique.setInspectionDate(freePratiqueReport.getInspectionDate());
                freePartique.setReportLink(freePratiqueReport.getReportLink());
                freePartique.setFirstRegisterId("TESWS");
                freePartique.setLastUpdateId("TESWS");
                freePartiqueRepository.save(freePartique);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.setDescription("Free partique Report with CommunicationAgreedId " + freePratiqueReport.getCommunicationAgreedId()
                + " is already present in Tancis");
        response.setCode(405);
        return response;
    }
}
