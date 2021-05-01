package com.ManifestTeswTancis.Service;

import com.ManifestTeswTancis.dtos.*;
import com.ManifestTeswTancis.dtos.TeswsResponse;

public interface ExImportManifestService {
	TeswsResponse createManifest(ManifestDto manifestDto);

}
