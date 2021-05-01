package com.ManifestTeswTancis.ServiceImpl;
import com.ManifestTeswTancis.Entity.VesselDepartureNoticeEntity;
import com.ManifestTeswTancis.Repository.VesselDepartureNoticeRepository;
import com.ManifestTeswTancis.Service.VesselDepartureService;
import com.ManifestTeswTancis.Util.DateFormatter;
import com.ManifestTeswTancis.Util.HttpMessage;
import com.ManifestTeswTancis.dtos.TeswsResponse;
import com.ManifestTeswTancis.dtos.VesselDepartureNoticeDto;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class VesselDepartureServiceImpl implements VesselDepartureService {

    final
    VesselDepartureNoticeRepository vesselDepartureNoticeRepository;

    public VesselDepartureServiceImpl(VesselDepartureNoticeRepository vesselDepartureNoticeRepository) {
        this.vesselDepartureNoticeRepository = vesselDepartureNoticeRepository;
    }


    @Override
    public TeswsResponse saveVesselDepartureNotice(VesselDepartureNoticeDto vesselDepartureNoticeDto) {
        TeswsResponse response = new TeswsResponse();
        response.setAckType("VESSEL_DEPARTURE_NOTICE");
        response.setCode(200);
        response.setAckDate(DateFormatter.getTeSWSLocalDate(LocalDateTime.now()));
        response.setDescription("Received Successfully");

        VesselDepartureNoticeEntity vsd= new VesselDepartureNoticeEntity();
        vsd.setCommunicationAgreedId(vesselDepartureNoticeDto.getCommunicationAgreedId());
        vsd.setVesselMaster(vesselDepartureNoticeDto.getVesselMaster());
        vsd.setVesselMasterAddress(vesselDepartureNoticeDto.getVesselMasterAddress());
        vsd.setAgentCode(vesselDepartureNoticeDto.getAgentCode());
        vsd.setAgentAddress(vesselDepartureNoticeDto.getAgentAddress());
        vsd.setVoyageNumber(vesselDepartureNoticeDto.getVoyageNumber());
        vsd.setCarrierId(vesselDepartureNoticeDto.getCarrierId());
        vsd.setModeOfTransport(vesselDepartureNoticeDto.getModeOfTransport());
        vsd.setCarrierName(vesselDepartureNoticeDto.getCarrierName());
        vsd.setTransportMeansId(vesselDepartureNoticeDto.getTransportMeansId());
        vsd.setTransportMeansName(vesselDepartureNoticeDto.getTransportMeansName());
        vsd.setTransportMeansNationality(vesselDepartureNoticeDto.getTransportMeansNationality());
        vsd.setVoyageNumberOutbound(vesselDepartureNoticeDto.getVoyageNumberOutbound());
        vsd.setTerminalOperatorCode(vesselDepartureNoticeDto.getTerminalOperatorCode());
        vsd.setDestinationPort(vesselDepartureNoticeDto.getDestinationPort());
        vsd.setTerminal(vesselDepartureNoticeDto.getTerminal());
        vsd.setPortOfCall(vesselDepartureNoticeDto.getPortOfCall());
        vsd.setNextPortOfCall(vesselDepartureNoticeDto.getNextPortOfCall());
        vsd.setEstimatedDatetimeOfArrival(vesselDepartureNoticeDto.getEstimatedDatetimeOfArrival());
        vsd.setEstimatedDatetimeOfDeparture(vesselDepartureNoticeDto.getEstimatedDatetimeOfDeparture());
        vsd.setActualDatetimeOfArrivalOuterAnchorage(vesselDepartureNoticeDto.getActualDatetimeOfArrivalOuterAnchorage());
        vsd.setHandoverDatetime(vesselDepartureNoticeDto.getHandoverDatetime());
        vsd.setActualDatetimeOfDeparture(vesselDepartureNoticeDto.getActualDatetimeOfDeparture());
        vsd.setActualDatetimeOfArrival(vesselDepartureNoticeDto.getActualDatetimeOfArrival());
        vsd.setActualDatetimeOfDepartureOuterAnchorage(vesselDepartureNoticeDto.getActualDatetimeOfDepartureOuterAnchorage());
        vsd.setControlReferenceNumber(vesselDepartureNoticeDto.getControlReferenceNumber());
        vsd.setMessageTypeId(vesselDepartureNoticeDto.getMessageTypeId());
        vsd.setMessageFunction(vesselDepartureNoticeDto.getMessageFunction());
        vsd.setTransportStageType(vesselDepartureNoticeDto.getTransportStageType());
        vsd.setModeOfTransportCoded(vesselDepartureNoticeDto.getModeOfTransportCoded());
        vsd.setRotationNumber(vesselDepartureNoticeDto.getRotationNumber());
        vsd.setBerthNo(vesselDepartureNoticeDto.getBerthNo());
        vsd.setQuantity(vesselDepartureNoticeDto.getQuantity());
        vsd.setQuantityUnit(vesselDepartureNoticeDto.getQuantityUnit());
        vsd.setQuantityOutbound(vesselDepartureNoticeDto.getQuantityOutbound());
        vsd.setQuantityUnitOutbound(vesselDepartureNoticeDto.getQuantityUnitOutbound());
        vsd.setDraftFore(vesselDepartureNoticeDto.getDraftFore());
        vsd.setDraftForeUnit(vesselDepartureNoticeDto.getDraftAfterUnit());
        vsd.setDraftAfter(vesselDepartureNoticeDto.getDraftAfter());
        vsd.setDraftAfterUnit(vesselDepartureNoticeDto.getDraftAfterUnit());
        vesselDepartureNoticeRepository.save(vsd);
//        HttpMessage httpMessage = new HttpMessage();
//        httpMessage.setContentType("application/json");
//        httpMessage.setMessageName("VESSEL_BOARDING_NOTIFICATION");
//        httpMessage.setRecipient("SS");
        return response;
    }
}
