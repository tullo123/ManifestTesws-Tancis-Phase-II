package com.ManifestTeswTancis.Service;

import com.ManifestTeswTancis.dtos.TeswsResponse;
import com.ManifestTeswTancis.dtos.VesselTrackingNotice;
import org.springframework.stereotype.Service;

@Service
public interface VesselTrackingService {
    TeswsResponse vesselTracking(VesselTrackingNotice vesselTrackingNotice);
}
