package com.ManifestTeswTancis.ServiceImpl;
import java.time.LocalDateTime;
import javax.transaction.Transactional;
import com.ManifestTeswTancis.Entity.LiquidBulkDischargeSequenceUpdateEntity;
import com.ManifestTeswTancis.Entity.PumpingSequenceDtoEntity;
import com.ManifestTeswTancis.Repository.LiquidBulkDischargeSequenceUpdateRepository;
import com.ManifestTeswTancis.Repository.PumpingSequenceDtoRepository;
import com.ManifestTeswTancis.Service.LiquidBulkDischargeSequenceUpdateService;
import com.ManifestTeswTancis.Util.DateFormatter;
import com.ManifestTeswTancis.dtos.LiquidBulkDischargeSequenceUpdateDto;
import com.ManifestTeswTancis.dtos.PumpingSequenceDto;
import com.ManifestTeswTancis.dtos.TeswsResponse;
import org.springframework.stereotype.Service;

@Service
public class LiquidBulkDischargeSequenceUpdateServiceImpl implements LiquidBulkDischargeSequenceUpdateService {

	final
	LiquidBulkDischargeSequenceUpdateRepository liquidBulkDischargeSequenceUpdateRepository;
	final
	PumpingSequenceDtoRepository pumpingSequenceDtoRepository;

	public LiquidBulkDischargeSequenceUpdateServiceImpl(LiquidBulkDischargeSequenceUpdateRepository liquidBulkDischargeSequenceUpdateRepository, PumpingSequenceDtoRepository pumpingSequenceDtoRepository) {
		this.liquidBulkDischargeSequenceUpdateRepository = liquidBulkDischargeSequenceUpdateRepository;
		this.pumpingSequenceDtoRepository = pumpingSequenceDtoRepository;
	}

	@Override
	@Transactional
	public TeswsResponse saveLiquidBulkDischargeSequenceUpdate(
			LiquidBulkDischargeSequenceUpdateDto liquidBulkDischargeSequenceUpdateDto) {
		TeswsResponse response = new TeswsResponse();
		response.setAckType("LIQUID_BULK_DISCHARGE_SEQUENCE_UPDATE");
		response.setRefId(liquidBulkDischargeSequenceUpdateDto.getControlReferenceNumber());
		response.setCode(200);
		response.setAckDate(DateFormatter.getTeSWSLocalDate(LocalDateTime.now()));
		response.setDescription("Liquid Bulk Discharge Sequence Update Received");
		
		LiquidBulkDischargeSequenceUpdateEntity lq = new LiquidBulkDischargeSequenceUpdateEntity();
		PumpingSequenceDtoEntity pe=new PumpingSequenceDtoEntity();
		lq.setRefNo(liquidBulkDischargeSequenceUpdateDto.getRefNo());
		lq.setVoyageNo(liquidBulkDischargeSequenceUpdateDto.getVoyageNo());
		lq.setMrn(liquidBulkDischargeSequenceUpdateDto.getMrn());
		lq.setCall_id(liquidBulkDischargeSequenceUpdateDto.getCall_id());
		lq.setVesselName(liquidBulkDischargeSequenceUpdateDto.getVesselName());
		lq.setImoNo(liquidBulkDischargeSequenceUpdateDto.getImoNo());
		lq.setDestinationPort(liquidBulkDischargeSequenceUpdateDto.getDestinationPort());
		lq.setRefdt(liquidBulkDischargeSequenceUpdateDto.getRefdt());
		lq.setBlQnt(liquidBulkDischargeSequenceUpdateDto.getBlQnt());
		lq.setProduct(liquidBulkDischargeSequenceUpdateDto.getProduct());
		liquidBulkDischargeSequenceUpdateRepository.save(lq);
		
		for (PumpingSequenceDto sq : liquidBulkDischargeSequenceUpdateDto.getPumpingSequence()) {
			pe.setTerminal(sq.getTerminal());
			pe.setQuantity(sq.getQuantity());
			pe.setRefNo(liquidBulkDischargeSequenceUpdateDto.getRefNo());
			pe.setFromDt(sq.getFromDt());
			pe.setToDt(sq.getToDt());
			pe.setDuration(sq.getDuration());
			pe.setCreatedAt(DateFormatter.getTeSWSLocalDate(LocalDateTime.now()));	
		}
		pumpingSequenceDtoRepository.save(pe);

		return response;
	}

}
