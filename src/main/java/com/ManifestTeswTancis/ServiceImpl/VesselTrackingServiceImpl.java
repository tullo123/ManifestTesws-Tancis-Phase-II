package com.ManifestTeswTancis.ServiceImpl;
import com.ManifestTeswTancis.Entity.InImportManifest;
import com.ManifestTeswTancis.Repository.InImportManifestRepository;
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
    final InImportManifestRepository inImportManifestRepository;

    public VesselTrackingServiceImpl(InImportManifestRepository inImportManifestRepository) {
        this.inImportManifestRepository = inImportManifestRepository;
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
            Optional<InImportManifest> optional=inImportManifestRepository.
                    findFirstByCommunicationAgreedId(vesselTrackingNotice.getCommunicationAgreedId());
            if (optional.isPresent()) {
                InImportManifest inImportManifest=optional.get();
                inImportManifest.setActualDateTimeOfArrival(DateFormatter.getDateFromLocalDateTime
                        (vesselTrackingNotice.getActualDatetimeOfArrival()));
                inImportManifest.setActualDatetimeOfDeparture(DateFormatter.getDateFromLocalDateTime
                        (vesselTrackingNotice.getActualDatetimeOfDeparture()));
                inImportManifestRepository.save(inImportManifest);
            }

        } catch (Exception e) {
            response.setDescription(e.getMessage());
            e.printStackTrace();
        }

        return response;
    }
}