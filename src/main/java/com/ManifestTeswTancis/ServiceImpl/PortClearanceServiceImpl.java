package com.ManifestTeswTancis.ServiceImpl;
import com.ManifestTeswTancis.Entity.PortClearanceNoticeEntity;
import com.ManifestTeswTancis.Repository.PortClearanceNoticeRepository;
import com.ManifestTeswTancis.Service.PortClearanceService;
import com.ManifestTeswTancis.Util.DateFormatter;
import com.ManifestTeswTancis.dtos.PortClearanceNoticeDto;
import com.ManifestTeswTancis.dtos.TeswsResponse;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class PortClearanceServiceImpl implements PortClearanceService {

    final
    PortClearanceNoticeRepository portClearanceNoticeRepository;

    public PortClearanceServiceImpl(PortClearanceNoticeRepository portClearanceNoticeRepository) {
        this.portClearanceNoticeRepository = portClearanceNoticeRepository;
    }

    @Override
    public TeswsResponse savePortClearanceNotice(PortClearanceNoticeDto portClearanceNoticeDto) {
        TeswsResponse response = new TeswsResponse();
        response.setAckType("PORT_CLEARANCE_NOTICE");
        response.setCode(200);
        response.setRefId(portClearanceNoticeDto.getCommunicationAgreedId());
        response.setAckDate(DateFormatter.getTeSWSLocalDate(LocalDateTime.now()));
        response.setDescription("Port Clearance Notice Received Successfully");

        PortClearanceNoticeEntity portClearanceNoticeEntity= new PortClearanceNoticeEntity();
        try{
            Optional<PortClearanceNoticeEntity> optional=portClearanceNoticeRepository.
                    findByCommunicationAgreedId(portClearanceNoticeDto.getCommunicationAgreedId());
            if(!optional.isPresent() && portClearanceNoticeDto.getApprovalStatus().contentEquals("APPROVED")) {
                    portClearanceNoticeEntity.setCommunicationAgreedId(portClearanceNoticeDto.getCommunicationAgreedId());
                    portClearanceNoticeEntity.setApprovalStatus(portClearanceNoticeDto.getApprovalStatus());
                    portClearanceNoticeEntity.setClearanceRef(portClearanceNoticeDto.getClearanceRef());
                    portClearanceNoticeEntity.setComment(portClearanceNoticeDto.getComment());
                    portClearanceNoticeEntity.setNoticeDate(portClearanceNoticeDto.getNoticeDate());
                    portClearanceNoticeEntity.setFirstRegisterId("TESWS");
                    portClearanceNoticeEntity.setLastUpdateId("TESWS");
                    portClearanceNoticeRepository.save(portClearanceNoticeEntity);
                } else if(optional.isPresent() || !portClearanceNoticeDto.getApprovalStatus().contentEquals("APPROVED")){
                System.out.println("Port Clearance Notice Not Accepted");
            }

        } catch (Exception e) {
            response.setDescription(e.getMessage());
            e.printStackTrace();
        }
        return response;
    }

}
