package com.ManifestTeswTancis.ServiceImpl;
import com.ManifestTeswTancis.Entity.ExImportManifest;
import com.ManifestTeswTancis.Repository.ExImportManifestRepository;
import com.ManifestTeswTancis.Service.VesselTrackingService;
import com.ManifestTeswTancis.Util.DateFormatter;
import com.ManifestTeswTancis.dtos.TeswsResponse;
import com.ManifestTeswTancis.dtos.VesselTrackingNotice;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class VesselTrackingServiceImpl implements VesselTrackingService {
    final
    ExImportManifestRepository exImportManifestRepository;

    public VesselTrackingServiceImpl(ExImportManifestRepository exImportManifestRepository) {
        this.exImportManifestRepository = exImportManifestRepository;
    }

    @Override
    public TeswsResponse vesselTracking(VesselTrackingNotice vesselTrackingNotice) {
        TeswsResponse response = new TeswsResponse();
        response.setAckType("VESSEL_TRACKING_NOTICE");
        response.setRefId(vesselTrackingNotice.getCommunicationAgreedId());
        response.setCode(200);
        response.setAckDate(DateFormatter.getTeSWSLocalDate(LocalDateTime.now()));
        response.setDescription("Vessel Tracking Received Successfully");

        try {
            Optional<ExImportManifest> optional=exImportManifestRepository.
                    findFirstByCommunicationAgreedId(vesselTrackingNotice.getCommunicationAgreedId());
            if (optional.isPresent()) {
                ExImportManifest inImportManifest=optional.get();
                inImportManifest.setActualDateTimeOfArrival(DateFormatter.getDateFromLocalDateTime
                        (vesselTrackingNotice.getActualDatetimeOfArrival()));
                inImportManifest.setActualDatetimeOfDeparture(DateFormatter.getDateFromLocalDateTime
                        (vesselTrackingNotice.getActualDatetimeOfDeparture()));
                exImportManifestRepository.save(inImportManifest);
            }

        } catch (Exception e) {
            response.setDescription(e.getMessage());
            e.printStackTrace();
        }

        return response;
    }
}