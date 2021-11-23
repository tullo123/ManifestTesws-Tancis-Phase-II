package com.ManifestTeswTancis.Service;

import com.ManifestTeswTancis.dtos.TeswsResponse;
import com.ManifestTeswTancis.dtos.VesselDocumentationDto;
import org.springframework.stereotype.Service;

@Service
public interface VesselDocumentationService {
    TeswsResponse vesselDocumentationReceiving(VesselDocumentationDto vesselDocumentationDto);
}
