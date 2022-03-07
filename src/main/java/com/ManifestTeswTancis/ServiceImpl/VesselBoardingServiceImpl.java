package com.ManifestTeswTancis.ServiceImpl;

import com.ManifestTeswTancis.Entity.ExImportManifest;
import com.ManifestTeswTancis.Repository.ExImportManifestRepository;
import com.ManifestTeswTancis.Service.VesselBoardingService;
import com.ManifestTeswTancis.Util.DateFormatter;
import com.ManifestTeswTancis.dtos.TeswsResponse;
import com.ManifestTeswTancis.dtos.VesselBoardingNotificationDto;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class VesselBoardingServiceImpl implements VesselBoardingService {
    final ExImportManifestRepository exImportManifestRepository;

    public VesselBoardingServiceImpl(ExImportManifestRepository exImportManifestRepository) {
        this.exImportManifestRepository = exImportManifestRepository;
    }

    @Override
    public TeswsResponse saveVesselBoarding(VesselBoardingNotificationDto vesselBoardingNotificationDto) {
        TeswsResponse response = new TeswsResponse();
        response.setAckType("VESSEL_BOARDING_NOTIFICATION");
        response.setRefId(vesselBoardingNotificationDto.getCommunicationAgreedId());
        response.setCode(200);
        response.setAckDate(DateFormatter.getTeSWSLocalDate(LocalDateTime.now()));
        response.setDescription("Vessel Boarding Notice Received Successfully");

        try{
            Optional<ExImportManifest> optional=exImportManifestRepository.
                    findFirstByCommunicationAgreedId(vesselBoardingNotificationDto.getCommunicationAgreedId());
            if(optional.isPresent()){
                ExImportManifest inImportManifest=optional.get();
                inImportManifest.setBoardingYn("Y");
                LocalDateTime localDateTime = LocalDateTime.now();
                inImportManifest.setBoardingDt(localDateTime);
                exImportManifestRepository.save(inImportManifest);
            }

            }catch (Exception e) {
            response.setDescription(e.getMessage());
                e.printStackTrace();
            }

        return response;
    }
}
