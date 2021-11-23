package com.ManifestTeswTancis.ServiceImpl;

import com.ManifestTeswTancis.Entity.FreePratiqueReportEntity;
import com.ManifestTeswTancis.Repository.FreePartiqueRepository;
import com.ManifestTeswTancis.Service.UpdateFreePartiqueService;
import com.ManifestTeswTancis.Util.DateFormatter;
import com.ManifestTeswTancis.dtos.FreePratiqueReportDto;
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
    public TeswsResponse updateFreePartiqueReport(FreePratiqueReportDto freePratiqueReportDto) {
        TeswsResponse response = new TeswsResponse();
        response.setAckDate(DateFormatter.getTeSWSLocalDate(LocalDateTime.now()));
        response.setRefId(freePratiqueReportDto.getCommunicationAgreedId());
        response.setDescription("Free Partique Report Update Received");
        response.setAckType("FREE_PRATIQUE_REPORT_UPDATE");

        try {
            Optional<FreePratiqueReportEntity> optional=freePartiqueRepository.
                    findFirstByCommunicationAgreedId(freePratiqueReportDto.getCommunicationAgreedId());
            if(optional.isPresent()){
                FreePratiqueReportEntity pratique=optional.get();
                pratique.setVesselMaster(freePratiqueReportDto.getVesselMaster());
                pratique.setVesselMasterAddress(freePratiqueReportDto.getVesselMasterAddress());
                pratique.setAgentCode(freePratiqueReportDto.getAgentCode());
                pratique.setAgentAddress(freePratiqueReportDto.getAgentAddress());
                pratique.setVoyageNumber(freePratiqueReportDto.getVoyageNumber());
                pratique.setCallSign(freePratiqueReportDto.getCallSign());
                pratique.setTransportMeansId(freePratiqueReportDto.getTransportMeansId());
                pratique.setTransportMeansName(freePratiqueReportDto.getTransportMeansName());
                pratique.setTransportMeansNationality(freePratiqueReportDto.getTransportMeansNationality());
                pratique.setInspectionVerdict(freePratiqueReportDto.getInspectionVerdict());
                pratique.setInspectionDate(freePratiqueReportDto.getInspectionDate());
                pratique.setReportLink(freePratiqueReportDto.getReportLink());
                freePartiqueRepository.save(pratique);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
