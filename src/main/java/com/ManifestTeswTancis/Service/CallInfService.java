package com.ManifestTeswTancis.Service;

import com.ManifestTeswTancis.Entity.ExportManifest;
import com.ManifestTeswTancis.dtos.TeswsResponse;
import com.ManifestTeswTancis.Entity.ExImportManifest;
import com.ManifestTeswTancis.Request.CallInfDetailsRequestModel;

import java.io.IOException;

public interface CallInfService {
	TeswsResponse createCallInfo(CallInfDetailsRequestModel callInfDetails);

	String submitCallInfoNotice(ExImportManifest storedCallInfDetails, ExportManifest exportManifest) throws IOException;

}
