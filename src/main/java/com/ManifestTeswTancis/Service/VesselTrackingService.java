package com.ManifestTeswTancis.Service;

import com.ManifestTeswTancis.dtos.TeswsResponse;
import com.ManifestTeswTancis.dtos.VesselTrackingNoticeDto;
import org.springframework.stereotype.Service;

@Service
public interface VesselTrackingService {
    TeswsResponse vesselTracking(VesselTrackingNoticeDto vesselTrackingNoticeDto);
}
