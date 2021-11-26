package com.ManifestTeswTancis.Service;
import com.ManifestTeswTancis.dtos.LiquidBulkDischargeSequenceDto;
import com.ManifestTeswTancis.dtos.TeswsResponse;
import org.springframework.stereotype.Service;
@Service
public interface LiquidBulkDischargeSequenceService {
	TeswsResponse saveLiquidBulkDischargeSequence(LiquidBulkDischargeSequenceDto liquidBulkDischargeSequenceDto);

}
