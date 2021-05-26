package com.ManifestTeswTancis.ServiceImpl;

import com.ManifestTeswTancis.Entity.VesselBoardingNotificationEntity;
import com.ManifestTeswTancis.Repository.VesselBoardingNotificationRepository;
import com.ManifestTeswTancis.Service.VesselBoardingService;
import com.ManifestTeswTancis.Util.DateFormatter;
import com.ManifestTeswTancis.dtos.TeswsResponse;
import com.ManifestTeswTancis.dtos.VesselBoardingNotificationDto;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
@Service
@Transactional
public class VesselBoardingServiceImpl implements VesselBoardingService {

    final
    VesselBoardingNotificationRepository vesselBoardingNotificationRepository;

    public VesselBoardingServiceImpl(VesselBoardingNotificationRepository vesselBoardingNotificationRepository) {
        this.vesselBoardingNotificationRepository = vesselBoardingNotificationRepository;
    }

    @Override
    public TeswsResponse saveVesselBoarding(VesselBoardingNotificationDto vesselBoardingNotificationDto) {
        TeswsResponse response = new TeswsResponse();
        response.setAckType("VESSEL_BOARDING_NOTIFICATION");
        response.setCode(200);
        response.setAckDate(DateFormatter.getTeSWSLocalDate(LocalDateTime.now()));
        response.setDescription("Vessel Boarding Notification Received Successfully");

        VesselBoardingNotificationEntity vb= new VesselBoardingNotificationEntity();
        vb.setActualDatetimeOfArrival(vesselBoardingNotificationDto.getActualDatetimeOfArrival());
        vb.setCommunicationAgreedId(vesselBoardingNotificationDto.getCommunicationAgreedId());
        vb.setVesselMaster(vesselBoardingNotificationDto.getVesselMaster());
        vb.setVesselMasterAddress(vesselBoardingNotificationDto.getVesselMasterAddress());
        vb.setAgentCode(vesselBoardingNotificationDto.getAgentCode());
        vb.setAgentAddress(vesselBoardingNotificationDto.getAgentAddress());
        vb.setVoyageNumber(vesselBoardingNotificationDto.getVoyageNumber());
        vb.setModeOfTransport(vesselBoardingNotificationDto.getModeOfTransport());
        vb.setCarrierId(vesselBoardingNotificationDto.getCarrierId());
        vb.setCarrierName(vesselBoardingNotificationDto.getCarrierName());
        vb.setTransportMeansId(vesselBoardingNotificationDto.getTransportMeansId());
        vb.setTransportMeansName(vesselBoardingNotificationDto.getTransportMeansName());
        vb.setTransportMeansNationality(vesselBoardingNotificationDto.getTransportMeansNationality());
        vb.setVoyageNumberOutbound(vesselBoardingNotificationDto.getVoyageNumberOutbound());
        vb.setTerminalOperatorCode(vesselBoardingNotificationDto.getTerminalOperatorCode());
        vb.setTerminal(vesselBoardingNotificationDto.getTerminal());
        vb.setDestinationPort(vesselBoardingNotificationDto.getDestinationPort());
        vb.setPortOfCall(vesselBoardingNotificationDto.getPortOfCall());
        vb.setNextPortOfCall(vesselBoardingNotificationDto.getNextPortOfCall());
        vb.setEstimatedDatetimeOfArrival(vesselBoardingNotificationDto.getEstimatedDatetimeOfArrival());
        vb.setEstimatedDatetimeOfDeparture(vesselBoardingNotificationDto.getEstimatedDatetimeOfDeparture());
        vb.setActualDatetimeOfArrival(vesselBoardingNotificationDto.getActualDatetimeOfArrival());
        vb.setCustomOfficeCode(vesselBoardingNotificationDto.getCustomOfficeCode());
        vesselBoardingNotificationRepository.save(vb);
        return response;
    }
}
