package com.ManifestTeswTancis.ServiceImpl;
import java.time.LocalDateTime;
import javax.transaction.Transactional;

import com.ManifestTeswTancis.Entity.LiquidBulkDischargeSequenceEntity;
import com.ManifestTeswTancis.Repository.LiquidBulkDischargeSequenceRepository;
import com.ManifestTeswTancis.Service.LiquidBulkDischargeSequenceService;
import com.ManifestTeswTancis.Util.DateFormatter;
import com.ManifestTeswTancis.dtos.LiquidBulkDischargeSequence;
import com.ManifestTeswTancis.dtos.PumpingSequence;
import com.ManifestTeswTancis.dtos.TeswsResponse;
import org.springframework.stereotype.Service;


@Service
public class LiquidBulkDischargeSequenceServiceImpl implements LiquidBulkDischargeSequenceService {

    final
	LiquidBulkDischargeSequenceRepository liquidBulkDischargeSequenceRepository;

	public LiquidBulkDischargeSequenceServiceImpl(LiquidBulkDischargeSequenceRepository liquidBulkDischargeSequenceRepository) {
		this.liquidBulkDischargeSequenceRepository = liquidBulkDischargeSequenceRepository;
	}

	@Override
	@Transactional
	public TeswsResponse saveLiquidBulkDischargeSequence(LiquidBulkDischargeSequence liquidBulkDischargeSequence) {
		TeswsResponse response = new TeswsResponse();
		response.setAckType("LIQUID_BULK_DISCHARGE_SEQUENCE");
		response.setRefId(liquidBulkDischargeSequence.getRefNo());
		response.setCode(200);
		response.setRefId(liquidBulkDischargeSequence.getRefNo());
		response.setAckDate(DateFormatter.getTeSWSLocalDate(LocalDateTime.now()));
		response.setDescription("Liquid Bulk Discharge Sequence Received");
		
		LiquidBulkDischargeSequenceEntity lq=new LiquidBulkDischargeSequenceEntity();
		lq.setRefNo(liquidBulkDischargeSequence.getRefNo());
		lq.setVoyageNo(liquidBulkDischargeSequence.getVoyageNo());
		lq.setVesselName(liquidBulkDischargeSequence.getVesselName());
		lq.setImoNo(liquidBulkDischargeSequence.getImoNo());
		lq.setCallSign(liquidBulkDischargeSequence.getCallSign());
		lq.setDestinationPort(liquidBulkDischargeSequence.getDestinationPort());
		lq.setRefDate(liquidBulkDischargeSequence.getRefDate());
		lq.setBlQnt(liquidBulkDischargeSequence.getBlQnt());
		lq.setOilType(liquidBulkDischargeSequence.getOilType());
		lq.setFirstRegisterId("TESWS");
		lq.setLastUpdateId("TESWS");
		
	for (PumpingSequence sq : liquidBulkDischargeSequence.getPumpingSequence()) {
		lq.setTerminal(sq.getTerminal());
		lq.setQuantity(sq.getQuantity());
		}

		liquidBulkDischargeSequenceRepository.save(lq);
		
		return response;
	}

}
