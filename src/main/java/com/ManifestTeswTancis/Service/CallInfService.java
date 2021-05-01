package com.ManifestTeswTancis.Service;

import com.ManifestTeswTancis.dtos.TeswsResponse;
import com.ManifestTeswTancis.Entity.ExImportManifest;
import com.ManifestTeswTancis.Entity.Request.CallInfDetailsRequestModel;

import java.io.IOException;

public interface CallInfService {
	TeswsResponse createCallInfo(CallInfDetailsRequestModel callInfDetails);

	String submitCallInfoNotice(ExImportManifest storedCallInfDetails) throws IOException;

}
