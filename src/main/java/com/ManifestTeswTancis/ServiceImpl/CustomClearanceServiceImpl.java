package com.ManifestTeswTancis.ServiceImpl;
import com.ManifestTeswTancis.Entity.CustomClearanceApprovalStatus;
import com.ManifestTeswTancis.Entity.CustomClearanceEntity;
import com.ManifestTeswTancis.Repository.CustomClearanceApprovalRepository;
import com.ManifestTeswTancis.Repository.CustomClearanceRepository;
import com.ManifestTeswTancis.Service.CustomClearanceService;
import com.ManifestTeswTancis.Util.DateFormatter;
import com.ManifestTeswTancis.dtos.CustomClearanceDto;
import com.ManifestTeswTancis.dtos.TeswsResponse;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CustomClearanceServiceImpl implements CustomClearanceService {

    final
    CustomClearanceRepository customClearanceRepository;
    final
    CustomClearanceApprovalRepository customClearanceApprovalRepository;

    public CustomClearanceServiceImpl(CustomClearanceRepository customClearanceRepository, CustomClearanceApprovalRepository customClearanceApprovalRepository) {
        this.customClearanceRepository = customClearanceRepository;
        this.customClearanceApprovalRepository = customClearanceApprovalRepository;
    }


    @Transactional
    @Override
    public TeswsResponse customService(CustomClearanceDto customClearanceDto) {
        TeswsResponse response = new TeswsResponse();
        response.setAckDate(DateFormatter.getTeSWSLocalDate(LocalDateTime.now()));
        response.setRefId(customClearanceDto.getCommunicationAgreedId());
        response.setDescription("Custom Clearance Request Received Successfully");
        response.setCode(200);
        response.setAckType("CUSTOM_CLEARANCE");

    try {
        Optional<CustomClearanceEntity> optional =customClearanceRepository.
                findByCommunicationAgreedId(customClearanceDto.getCommunicationAgreedId());
        if(!optional.isPresent()){
            CustomClearanceEntity cu= new CustomClearanceEntity();
            cu.setCommunicationAgreedId(customClearanceDto.getCommunicationAgreedId());
            cu.setProcessingStatus("B");
            cu.setVesselMaster(customClearanceDto.getVesselMaster());
            cu.setVesselMasterAddress(customClearanceDto.getVesselMasterAddress());
            cu.setAgentCode(customClearanceDto.getAgentCode());
            cu.setAgentAddress(customClearanceDto.getAgentAddress());
            cu.setVoyageNumber(customClearanceDto.getVoyageNumber());
            cu.setCarrierId(customClearanceDto.getCarrierId());
            cu.setCarrierName(customClearanceDto.getCarrierName());
            cu.setTransportMeansName(customClearanceDto.getTransportMeansName());
            cu.setTransportMeansNationality(customClearanceDto.getTransportMeansNationality());
            cu.setVoyageNumberOutbound(customClearanceDto.getVoyageNumberOutbound());
            cu.setTerminal(customClearanceDto.getTerminal());
            cu.setDestinationPort(customClearanceDto.getDestinationPort());
            cu.setNextPortOfCall(customClearanceDto.getNextPortOfCall());
            cu.setPortOfCall(customClearanceDto.getPortOfCall());
            cu.setTaxClearanceNumber(customClearanceDto.getTaxClearanceNumber());
            cu.setLastUpdateId("TESWS");
            cu.setFirstRegisterId("TESWS");
            cu.setEstimatedDatetimeOfArrival(DateFormatter.getDateFromLocalDateTime(customClearanceDto.getEstimatedDatetimeOfArrival()));
            cu.setActualDatetimeOfArrival(DateFormatter.getDateFromLocalDateTime(customClearanceDto.getActualDatetimeOfArrival()));
            cu.setClearanceRequestDate(DateFormatter.getDateFromLocalDateTime(customClearanceDto.getClearanceRequestDate()));
            cu.setEstimatedDatetimeOfDeparture(DateFormatter.getDateFromLocalDateTime(customClearanceDto.getEstimatedDatetimeOfDeparture()));

            customClearanceRepository.save(cu);

            CustomClearanceApprovalStatus cs= new CustomClearanceApprovalStatus();
            cs.setCommunicationAgreedId(customClearanceDto.getCommunicationAgreedId());
            cs.setTaxClearanceNumber(customClearanceDto.getTaxClearanceNumber());
            cs.setVoyageNumber(customClearanceDto.getVoyageNumber());
            customClearanceApprovalRepository.save(cs);
        }

    } catch (Exception exception) {
        exception.printStackTrace();
    }
        return response;
    }

}
