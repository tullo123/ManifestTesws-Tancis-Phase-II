package com.ManifestTeswTancis.ServiceImpl;
import java.time.LocalDateTime;
import javax.transaction.Transactional;

import com.ManifestTeswTancis.Entity.LiquidBulkDischargeSequenceEntity;
import com.ManifestTeswTancis.Repository.LiquidBulkDischargeSequenceRepository;
import com.ManifestTeswTancis.Service.LiquidBulkDischargeSequenceService;
import com.ManifestTeswTancis.Util.DateFormatter;
import com.ManifestTeswTancis.dtos.LiquidBulkDischargeSequenceDto;
import com.ManifestTeswTancis.dtos.PumpingSequenceDto;
import com.ManifestTeswTancis.dtos.TeswsResponse;
import org.springframework.stereotype.Service;


@Service
public class LiquidBulkDischargeSequenceServiceImpl implements LiquidBulkDischargeSequenceService {

    final LiquidBulkDischargeSequenceRepository liquidBulkDischargeSequenceRepository;

	public LiquidBulkDischargeSequenceServiceImpl(LiquidBulkDischargeSequenceRepository liquidBulkDischargeSequenceRepository) {
		this.liquidBulkDischargeSequenceRepository = liquidBulkDischargeSequenceRepository;
	}

	@Override
	@Transactional
	public TeswsResponse saveLiquidBulkDischargeSequence(LiquidBulkDischargeSequenceDto liquidBulkDischargeSequenceDto) {
		TeswsResponse response = new TeswsResponse();
		response.setAckType("LIQUID_BULK_DISCHARGE_SEQUENCE");
		response.setRefId(liquidBulkDischargeSequenceDto.getRefNo());
		response.setCode(200);
		response.setRefId(liquidBulkDischargeSequenceDto.getRefNo());
		response.setAckDate(DateFormatter.getTeSWSLocalDate(LocalDateTime.now()));
		response.setDescription("Liquid Bulk Discharge Sequence Received");
		
		LiquidBulkDischargeSequenceEntity lq=new LiquidBulkDischargeSequenceEntity();
		lq.setRefNo(liquidBulkDischargeSequenceDto.getRefNo());
		lq.setVoyageNo(liquidBulkDischargeSequenceDto.getVoyageNo());
		lq.setVesselName(liquidBulkDischargeSequenceDto.getVesselName());
		lq.setImoNo(liquidBulkDischargeSequenceDto.getImoNo());
		lq.setCallSign(liquidBulkDischargeSequenceDto.getCallSign());
		lq.setDestinationPort(liquidBulkDischargeSequenceDto.getDestinationPort());
		lq.setRefDate(liquidBulkDischargeSequenceDto.getRefDate());
		lq.setBlQnt(liquidBulkDischargeSequenceDto.getBlQnt());
		lq.setOilType(liquidBulkDischargeSequenceDto.getOilType());
		lq.setFirstRegisterId("TESWS");
		lq.setLastUpdateId("TESWS");
		
	for (PumpingSequenceDto sq : liquidBulkDischargeSequenceDto.getPumpingSequence()) {
		lq.setTerminal(sq.getTerminal());
		lq.setQuantity(sq.getQuantity());
		}

		liquidBulkDischargeSequenceRepository.save(lq);
		
		return response;
	}

}
