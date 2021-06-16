package com.ManifestTeswTancis.ServiceImpl;

import com.ManifestTeswTancis.Entity.FreePratiqueReportEntity;
import com.ManifestTeswTancis.Repository.FreePartiqueRepository;
import com.ManifestTeswTancis.Service.UpdateFreePartiqueService;
import com.ManifestTeswTancis.Util.DateFormatter;
import com.ManifestTeswTancis.dtos.FreePratiqueReport;
import com.ManifestTeswTancis.dtos.TeswsResponse;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class UpdateFreePartiqueServiceImpl implements UpdateFreePartiqueService {
    final
    FreePartiqueRepository freePartiqueRepository;

    public UpdateFreePartiqueServiceImpl(FreePartiqueRepository freePartiqueRepository) {
        this.freePartiqueRepository = freePartiqueRepository;
    }

    @Override
    public TeswsResponse updateFreePartiqueReport(FreePratiqueReport freePratiqueReport) {
        TeswsResponse response = new TeswsResponse();
        response.setAckDate(DateFormatter.getTeSWSLocalDate(LocalDateTime.now()));
        response.setRefId(freePratiqueReport.getCommunicationAgreedId());
        response.setDescription("Free Partique Report Update Received");
        response.setAckType("FREE_PRATIQUE_REPORT_UPDATE");

        try {
            Optional<FreePratiqueReportEntity> optional=freePartiqueRepository.
                    findFirstByCommunicationAgreedId(freePratiqueReport.getCommunicationAgreedId());
            if(optional.isPresent()){
                FreePratiqueReportEntity pratique=optional.get();
                pratique.setVesselMaster(freePratiqueReport.getVesselMaster());
                pratique.setVesselMasterAddress(freePratiqueReport.getVesselMasterAddress());
                pratique.setAgentCode(freePratiqueReport.getAgentCode());
                pratique.setAgentAddress(freePratiqueReport.getAgentAddress());
                pratique.setVoyageNumber(freePratiqueReport.getVoyageNumber());
                pratique.setCallSign(freePratiqueReport.getCallSign());
                pratique.setTransportMeansId(freePratiqueReport.getTransportMeansId());
                pratique.setTransportMeansName(freePratiqueReport.getTransportMeansName());
                pratique.setTransportMeansNationality(freePratiqueReport.getTransportMeansNationality());
                pratique.setInspectionVerdict(freePratiqueReport.getInspectionVerdict());
                pratique.setInspectionDate(freePratiqueReport.getInspectionDate());
                pratique.setReportLink(freePratiqueReport.getReportLink());
                freePartiqueRepository.save(pratique);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
