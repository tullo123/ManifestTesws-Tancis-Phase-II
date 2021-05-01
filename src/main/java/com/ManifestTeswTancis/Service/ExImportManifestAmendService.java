
package com.ManifestTeswTancis.Service;

import com.ManifestTeswTancis.dtos.*;
import com.ManifestTeswTancis.dtos.TeswsResponse;
import org.springframework.stereotype.Service;

@Service
public interface ExImportManifestAmendService {
	TeswsResponse amendManifest(ManifestAmendmentDto manifestAmendmentDto);

}