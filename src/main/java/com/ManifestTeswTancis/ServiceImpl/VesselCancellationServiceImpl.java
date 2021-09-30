package com.ManifestTeswTancis.ServiceImpl;
import com.ManifestTeswTancis.Entity.ExImportManifest;
import com.ManifestTeswTancis.Repository.ExImportManifestRepository;
import com.ManifestTeswTancis.Service.VesselCancellationService;
import com.ManifestTeswTancis.Util.DateFormatter;
import com.ManifestTeswTancis.dtos.PortCallIdCancellationDto;
import com.ManifestTeswTancis.dtos.TeswsResponse;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class VesselCancellationServiceImpl implements VesselCancellationService {
    final
    ExImportManifestRepository exImportManifestRepository;

    public VesselCancellationServiceImpl(ExImportManifestRepository exImportManifestRepository) {
        this.exImportManifestRepository = exImportManifestRepository;
    }

    @Override
    @Transactional
    public TeswsResponse cancelVesselInfo(PortCallIdCancellationDto portCallIdCancellationDto) {
        TeswsResponse response = new TeswsResponse();
        response.setAckDate(DateFormatter.getTeSWSLocalDate(LocalDateTime.now()));
       response.setRefId(portCallIdCancellationDto.getCancelRef());
        response.setDescription("Vessel Cancellation Notice received successfully");
        response.setAckType("VESSEL_CANCELLATION_NOTICE");

        try {
            Optional<ExImportManifest> optional = exImportManifestRepository
                    .findFirstByCommunicationAgreedId(portCallIdCancellationDto.getCommunicationAgreedId());
            if(optional.isPresent()) {
                ExImportManifest ex = optional.get();
                ex.setProcessingStatus("X");
                exImportManifestRepository.save(ex);
            }
        } catch (Exception e) {
            response.setDescription(e.getMessage());
            response.setCode(400);
            System.err.println("CommunicationAgreedId:"+ portCallIdCancellationDto.getCommunicationAgreedId()+" Not Found");
        }
        return response;
    }
}
