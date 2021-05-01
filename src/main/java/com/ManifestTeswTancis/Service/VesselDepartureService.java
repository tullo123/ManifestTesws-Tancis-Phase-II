package com.ManifestTeswTancis.Service;

import com.ManifestTeswTancis.dtos.TeswsResponse;
import org.springframework.stereotype.Service;
import com.ManifestTeswTancis.dtos.*;

@Service
public interface VesselDepartureService {

    TeswsResponse saveVesselDepartureNotice(VesselDepartureNoticeDto vesselDepartureNoticeDto);
}
