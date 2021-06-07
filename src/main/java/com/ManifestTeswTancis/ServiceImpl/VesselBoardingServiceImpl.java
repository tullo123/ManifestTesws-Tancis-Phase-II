package com.ManifestTeswTancis.ServiceImpl;

import com.ManifestTeswTancis.Entity.InImportManifest;
import com.ManifestTeswTancis.Repository.InImportManifestRepository;
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
    final
    InImportManifestRepository inImportManifestRepository;

    public VesselBoardingServiceImpl(InImportManifestRepository inImportManifestRepository) {
        this.inImportManifestRepository = inImportManifestRepository;
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
            Optional<InImportManifest> optional=inImportManifestRepository.
                    findFirstByCommunicationAgreedId(vesselBoardingNotificationDto.getCommunicationAgreedId());
            if(optional.isPresent()){
                InImportManifest inImportManifest=optional.get();
                inImportManifest.setBoardingYn("Y");
                inImportManifest.setBoardingDt(DateFormatter.getDateFromLocalDateTime(LocalDateTime.now()));
                inImportManifestRepository.save(inImportManifest);
            }

            }catch (Exception e) {
                e.printStackTrace();
            }

        return response;
    }
}
