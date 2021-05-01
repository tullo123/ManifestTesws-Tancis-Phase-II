package com.ManifestTeswTancis.Service;
import com.ManifestTeswTancis.dtos.LiquidBulkDischargeSequence;
import com.ManifestTeswTancis.dtos.TeswsResponse;
import org.springframework.stereotype.Service;
@Service
public interface LiquidBulkDischargeSequenceService {
	TeswsResponse saveLiquidBulkDischargeSequence(LiquidBulkDischargeSequence liquidBulkDischargeSequence);

}
