package com.ManifestTeswTancis.Service;

import com.ManifestTeswTancis.dtos.TeswsResponse;
import com.ManifestTeswTancis.dtos.VesselDocumentation;
import org.springframework.stereotype.Service;

@Service
public interface VesselDocumentationService {
    TeswsResponse vesselDocumentationReceiving(VesselDocumentation vesselDocumentation);
}
