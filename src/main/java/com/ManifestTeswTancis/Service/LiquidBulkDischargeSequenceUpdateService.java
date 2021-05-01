package com.ManifestTeswTancis.Service;

import com.ManifestTeswTancis.dtos.LiquidBulkDischargeSequenceUpdateDto;
import com.ManifestTeswTancis.dtos.TeswsResponse;
import org.springframework.stereotype.Service;
@Service
public interface LiquidBulkDischargeSequenceUpdateService {
	TeswsResponse saveLiquidBulkDischargeSequenceUpdate(LiquidBulkDischargeSequenceUpdateDto liquidBulkDischargeSequenceUpdateDto );

}
