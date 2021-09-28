package com.ManifestTeswTancis.ServiceImpl;
import com.ManifestTeswTancis.Entity.InImportManifest;
import com.ManifestTeswTancis.Repository.InImportManifestRepository;
import com.ManifestTeswTancis.Service.VesselDepartureService;
import com.ManifestTeswTancis.Util.DateFormatter;
import com.ManifestTeswTancis.dtos.TeswsResponse;
import com.ManifestTeswTancis.dtos.VesselDepartureNoticeDto;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class VesselDepartureServiceImpl implements VesselDepartureService {
    final InImportManifestRepository inImportManifestRepository;

    public VesselDepartureServiceImpl(InImportManifestRepository inImportManifestRepository) {
        this.inImportManifestRepository = inImportManifestRepository;
    }


    @Override
    public TeswsResponse saveVesselDepartureNotice(VesselDepartureNoticeDto vesselDepartureNoticeDto) {
        TeswsResponse response = new TeswsResponse();
        response.setAckType("VESSEL_DEPARTURE_NOTICE");
        response.setCode(200);
        response.setRefId(vesselDepartureNoticeDto.getCommunicationAgreedId());
        response.setAckDate(DateFormatter.getTeSWSLocalDate(LocalDateTime.now()));
        response.setDescription("Vessel Departure Notice Received Successfully");

        try {
            Optional<InImportManifest> optional=inImportManifestRepository.
                    findFirstByCommunicationAgreedId(vesselDepartureNoticeDto.getCommunicationAgreedId());
            if (optional.isPresent()) {
                InImportManifest inImportManifest=optional.get();
                inImportManifest.setActualDatetimeOfDeparture(DateFormatter.getDateFromLocalDateTime
                        (vesselDepartureNoticeDto.getActualDatetimeOfDeparture()));
                inImportManifestRepository.save(inImportManifest);
            }

        } catch (Exception e) {
            response.setDescription(e.getMessage());
            e.printStackTrace();
        }
        return response;
    }
}
