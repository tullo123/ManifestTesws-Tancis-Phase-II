package com.ManifestTeswTancis.ServiceImpl;
import com.ManifestTeswTancis.Entity.PortClearanceNoticeEntity;
import com.ManifestTeswTancis.Repository.PortClearanceNoticeRepository;
import com.ManifestTeswTancis.Service.PortClearanceService;
import com.ManifestTeswTancis.Util.DateFormatter;
import com.ManifestTeswTancis.Util.HttpMessage;
import com.ManifestTeswTancis.dtos.PortClearanceNoticeDto;
import com.ManifestTeswTancis.dtos.TeswsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@Transactional
public class PortClearanceServiceImpl implements PortClearanceService {

    @Autowired
    PortClearanceNoticeRepository portClearanceNoticeRepository;
    @Override
    public TeswsResponse savePortClearanceNotice(PortClearanceNoticeDto portClearanceNoticeDto) {
        TeswsResponse response = new TeswsResponse();
        response.setAckType("PORT_CLEARANCE_NOTICE");
        response.setCode(200);
        response.setAckDate(DateFormatter.getTeSWSLocalDate(LocalDateTime.now()));
        response.setDescription("Received Successfully");

        PortClearanceNoticeEntity portClearanceNoticeEntity= new PortClearanceNoticeEntity();
        portClearanceNoticeEntity.setCallId(portClearanceNoticeDto.getCallId());
        portClearanceNoticeEntity.setApprovalStatus(portClearanceNoticeDto.getApprovalStatus());
        portClearanceNoticeEntity.setClearanceReference(portClearanceNoticeDto.getClearanceReference());
        portClearanceNoticeEntity.setComment(portClearanceNoticeDto.getComment());
        portClearanceNoticeEntity.setNoticeDate(portClearanceNoticeDto.getNoticeDate());
        portClearanceNoticeRepository.save(portClearanceNoticeEntity);

        HttpMessage httpMessage = new HttpMessage();
        httpMessage.setContentType("application/json");
        httpMessage.setMessageName("CUSTOMS_VESSEL_REFERENCE");
        httpMessage.setRecipient("SS");
        return response;
    }


}
