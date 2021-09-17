package com.ManifestTeswTancis.Service;

import com.ManifestTeswTancis.Entity.ExportManifest;
import com.ManifestTeswTancis.dtos.TeswsResponse;
import com.ManifestTeswTancis.Entity.ExImportManifest;
import com.ManifestTeswTancis.Request.PortCallIdRequestModel;

import java.io.IOException;

public interface PortCallIdService {
	TeswsResponse createCallInfo(PortCallIdRequestModel callInfDetails);

	String submitCallInfoNotice(ExImportManifest storedCallInfDetails, ExportManifest exportManifest) throws IOException;

}
