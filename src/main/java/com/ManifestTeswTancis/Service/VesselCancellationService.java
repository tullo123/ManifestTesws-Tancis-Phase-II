package com.ManifestTeswTancis.Service;

import com.ManifestTeswTancis.dtos.PortCallIdCancellationDto;
import com.ManifestTeswTancis.dtos.TeswsResponse;
import org.springframework.stereotype.Service;

@Service
public interface VesselCancellationService {
    TeswsResponse cancelVesselInfo(PortCallIdCancellationDto portCallIdCancellationDto);
}
