package com.ManifestTeswTancis.Service;

import com.ManifestTeswTancis.dtos.ExportManifestDto;
import com.ManifestTeswTancis.dtos.TeswsResponse;
import org.springframework.stereotype.Service;

@Service
public interface ExportManifestService {
    TeswsResponse createExportManifest(ExportManifestDto exportManifestDto);
}
