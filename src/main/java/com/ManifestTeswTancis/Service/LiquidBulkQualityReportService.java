package com.ManifestTeswTancis.Service;

import com.ManifestTeswTancis.dtos.LiquidBulkQualityReportDto;
import com.ManifestTeswTancis.dtos.TeswsResponse;
import org.springframework.stereotype.Service;
@Service
public interface LiquidBulkQualityReportService {
	TeswsResponse saveLiquidBulkQualityReport(LiquidBulkQualityReportDto liquidBulkQualityReportDto);

}
