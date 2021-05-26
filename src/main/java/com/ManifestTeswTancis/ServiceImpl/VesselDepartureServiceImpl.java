package com.ManifestTeswTancis.ServiceImpl;
import com.ManifestTeswTancis.Entity.VesselDepartureNoticeEntity;
import com.ManifestTeswTancis.Repository.VesselDepartureNoticeRepository;
import com.ManifestTeswTancis.Service.VesselDepartureService;
import com.ManifestTeswTancis.Util.DateFormatter;
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
        response.setDescription("Vessel Departure Notice Received Successfully");

        VesselDepartureNoticeEntity vsd= new VesselDepartureNoticeEntity();
        vsd.setCommunicationAgreedId(vesselDepartureNoticeDto.getCommunicationAgreedId());
        vsd.setVesselMaster(vesselDepartureNoticeDto.getVesselMaster());
        vsd.setVesselMasterAddress(vesselDepartureNoticeDto.getVesselMasterAddress());
        vsd.setAgentCode(vesselDepartureNoticeDto.getAgentCode());
        vsd.setAgentAddress(vesselDepartureNoticeDto.getAgentAddress());
        vsd.setVoyageNumber(vesselDepartureNoticeDto.getVoyageNumber());
        vsd.setCarrierId(vesselDepartureNoticeDto.getCarrierId());
        vsd.setModeOfTransport(vesselDepartureNoticeDto.getModeOfTransport());
        vsd.setTransportMeansId(vesselDepartureNoticeDto.getTransportMeansId());
        vsd.setTransportMeansName(vesselDepartureNoticeDto.getTransportMeansName());
        vsd.setTransportMeansNationality(vesselDepartureNoticeDto.getTransportMeansNationality());
        vsd.setNextPortOfCall(vesselDepartureNoticeDto.getNextPortOfCall());
        vsd.setHandoverDatetime(vesselDepartureNoticeDto.getHandoverDatetime());
        vsd.setActualDatetimeOfDeparture(vesselDepartureNoticeDto.getActualDatetimeOfDeparture());
        vsd.setControlReferenceNumber(vesselDepartureNoticeDto.getControlReferenceNumber());
        vsd.setMessageTypeId(vesselDepartureNoticeDto.getMessageTypeId());
        vsd.setMessageFunction(vesselDepartureNoticeDto.getMessageFunction());
        vsd.setModeOfTransportCoded(vesselDepartureNoticeDto.getModeOfTransportCoded());
        vsd.setRotationNumber(vesselDepartureNoticeDto.getRotationNumber());
        vsd.setActualDatetimeOfDepartureOuterAnchorage(vesselDepartureNoticeDto.getActualDatetimeOfDepartureOuterAnchorage());
        vesselDepartureNoticeRepository.save(vsd);
        return response;
    }
}
