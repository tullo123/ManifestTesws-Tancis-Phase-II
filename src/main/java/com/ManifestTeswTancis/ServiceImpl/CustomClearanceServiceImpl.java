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
        cu.setEstimatedDatetimeOfArrival(customClearanceDto.getEstimatedDatetimeOfArrival());
        cu.setEstimatedDatetimeOfDeparture(customClearanceDto.getEstimatedDatetimeOfDeparture());
        cu.setActualDatetimeOfArrival(customClearanceDto.getActualDatetimeOfArrival());
        cu.setClearanceRequestDate(customClearanceDto.getClearanceRequestDate());
        cu.setFirstRegisterId("TESWS");
        cu.setLastUpdateId("TESWS");
        customClearanceRepository.save(cu);

        CustomClearanceApprovalStatus cs= new CustomClearanceApprovalStatus();
        cs.setCommunicationAgreedId(customClearanceDto.getCommunicationAgreedId());
        cs.setTaxClearanceNumber(customClearanceDto.getTaxClearanceNumber());
        cs.setVoyageNumber(customClearanceDto.getVoyageNumber());
        customClearanceApprovalRepository.save(cs);

        return response;
    }

}
