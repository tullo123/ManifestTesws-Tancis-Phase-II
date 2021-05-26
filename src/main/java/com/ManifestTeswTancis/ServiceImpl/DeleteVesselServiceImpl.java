package com.ManifestTeswTancis.ServiceImpl;
import com.ManifestTeswTancis.Entity.ExImportManifest;
import com.ManifestTeswTancis.Repository.ExImportManifestRepository;
import com.ManifestTeswTancis.Service.DeleteVesselService;
import com.ManifestTeswTancis.Util.DateFormatter;
import com.ManifestTeswTancis.dtos.CallInfCancelDto;
import com.ManifestTeswTancis.dtos.TeswsResponse;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@DynamicUpdate
public class DeleteVesselServiceImpl implements DeleteVesselService {
    final
    ExImportManifestRepository exImportManifestRepository;

    public DeleteVesselServiceImpl(ExImportManifestRepository exImportManifestRepository) {
        this.exImportManifestRepository = exImportManifestRepository;
    }

    @Override
    @Transactional
    public TeswsResponse deleteVesselInfo(CallInfCancelDto callInfCancelDto) {
        TeswsResponse response = new TeswsResponse();
        response.setAckDate(DateFormatter.getTeSWSLocalDate(LocalDateTime.now()));
       response.setRefId(callInfCancelDto.getControlReferenceNumber());
        response.setDescription("Cancellation of vessel call received successfully");
        response.setAckType("VESSEL_CALL_DELETE");

        try {
            Optional<ExImportManifest> optional = exImportManifestRepository
                    .findFirstByMrn(callInfCancelDto.getMrn());
            if(optional.isPresent()) {
                ExImportManifest ex = new ExImportManifest();
                ex.setProcessingStatus("X");
                exImportManifestRepository.save(ex);
            }
        } catch (Exception e) {
            response.setDescription(e.getMessage());
            response.setCode(400);
            System.err.println("vessel cancellation request with Mrn"+callInfCancelDto.getMrn()+" Not Found");
        }
        return response;
    }
}
