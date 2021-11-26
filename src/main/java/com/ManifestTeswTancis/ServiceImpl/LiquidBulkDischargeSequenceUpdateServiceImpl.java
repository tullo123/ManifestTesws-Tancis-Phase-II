package com.ManifestTeswTancis.ServiceImpl;
import java.time.LocalDateTime;
import java.util.Optional;
import javax.transaction.Transactional;

import com.ManifestTeswTancis.Entity.LiquidBulkDischargeSequenceEntity;
import com.ManifestTeswTancis.Repository.LiquidBulkDischargeSequenceRepository;
import com.ManifestTeswTancis.Service.LiquidBulkDischargeSequenceUpdateService;
import com.ManifestTeswTancis.Util.DateFormatter;
import com.ManifestTeswTancis.dtos.LiquidBulkDischargeSequenceUpdateDto;
import com.ManifestTeswTancis.dtos.PumpingSequenceDto;
import com.ManifestTeswTancis.dtos.TeswsResponse;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class LiquidBulkDischargeSequenceUpdateServiceImpl implements LiquidBulkDischargeSequenceUpdateService {

	final LiquidBulkDischargeSequenceRepository liquidBulkDischargeSequenceRepository;

	public LiquidBulkDischargeSequenceUpdateServiceImpl(LiquidBulkDischargeSequenceRepository liquidBulkDischargeSequenceRepository) {
		this.liquidBulkDischargeSequenceRepository = liquidBulkDischargeSequenceRepository;
	}

	@Override
	public TeswsResponse saveLiquidBulkDischargeSequenceUpdate(
			LiquidBulkDischargeSequenceUpdateDto liquidBulkDischargeSequenceUpdateDto) {
		TeswsResponse response = new TeswsResponse();
		response.setAckType("LIQUID_BULK_DISCHARGE_SEQUENCE_UPDATE");
		response.setRefId(liquidBulkDischargeSequenceUpdateDto.getRefNo());
		response.setCode(200);
		response.setAckDate(DateFormatter.getTeSWSLocalDate(LocalDateTime.now()));
		response.setDescription("Liquid Bulk Discharge Sequence Update Received");

		try{
			Optional<LiquidBulkDischargeSequenceEntity> optional=liquidBulkDischargeSequenceRepository.
					findFirstByRefNo(liquidBulkDischargeSequenceUpdateDto.getRefNo());
			if(optional.isPresent()){
				LiquidBulkDischargeSequenceEntity liquid=optional.get();
				liquid.setMrn(liquidBulkDischargeSequenceUpdateDto.getMrn());
				liquid.setCommunicationAgreedId(liquidBulkDischargeSequenceUpdateDto.getCommunicationAgreedId());
				for(PumpingSequenceDto p:liquidBulkDischargeSequenceUpdateDto.getPumpingSequence()){
					liquid.setStartDateTime(p.getStartDateTime());
					liquid.setEndDateTime(p.getEndDateTime());
				}
				liquidBulkDischargeSequenceRepository.save(liquid);
			}

		}catch (Exception e) {
			e.printStackTrace();
		}

		return response;
	}

}
