package com.ManifestTeswTancis.ServiceImpl;
import java.time.LocalDateTime;
import javax.transaction.Transactional;

import com.ManifestTeswTancis.Entity.LiquidBulkDischargeSequenceEntity;
import com.ManifestTeswTancis.Entity.PumpigSequenceEntity;
import com.ManifestTeswTancis.Repository.LiquidBulkDischargeSequenceRepository;
import com.ManifestTeswTancis.Repository.PumpingRepository;
import com.ManifestTeswTancis.Service.LiquidBulkDischargeSequenceService;
import com.ManifestTeswTancis.Util.DateFormatter;
import com.ManifestTeswTancis.dtos.LiquidBulkDischargeSequence;
import com.ManifestTeswTancis.dtos.PumpingSequence;
import com.ManifestTeswTancis.dtos.TeswsResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


@Service
public class LiquidBulkDischargeSequenceServiceImpl implements LiquidBulkDischargeSequenceService {

	final
	LiquidBulkDischargeSequenceRepository liquidBulkDischargeSequenceRepository;
	final
	PumpingRepository pumpingRepository;
	final
	JdbcTemplate jdbcTemplate;

	public LiquidBulkDischargeSequenceServiceImpl(LiquidBulkDischargeSequenceRepository liquidBulkDischargeSequenceRepository, PumpingRepository pumpingRepository, JdbcTemplate jdbcTemplate) {
		this.liquidBulkDischargeSequenceRepository = liquidBulkDischargeSequenceRepository;
		this.pumpingRepository = pumpingRepository;
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	@Transactional
	public TeswsResponse saveLiquidBulkDischargeSequence(LiquidBulkDischargeSequence liquidBulkDischargeSequence) {
		TeswsResponse response = new TeswsResponse();
		response.setAckType("LIQUID_BULK_DISCHARGE_SEQUENCE");
		response.setRefId(liquidBulkDischargeSequence.getControlReferenceNumber());
		response.setCode(200);
		response.setRefId(liquidBulkDischargeSequence.getRefNo());
		response.setAckDate(DateFormatter.getTeSWSLocalDate(LocalDateTime.now()));
		response.setDescription("Liquid Bulk Discharge Sequence Received");
		
		LiquidBulkDischargeSequenceEntity lq=new LiquidBulkDischargeSequenceEntity();
		PumpigSequenceEntity pe=new PumpigSequenceEntity();
		lq.setRefNo(liquidBulkDischargeSequence.getRefNo());
		lq.setVoyageNo(liquidBulkDischargeSequence.getVoyageNo());
		lq.setDestinationPort(liquidBulkDischargeSequence.getDestinationPort());
		lq.setImoNo(liquidBulkDischargeSequence.getImoNo());
		lq.setVesselName(liquidBulkDischargeSequence.getVesselName());
		lq.setBlQnt(liquidBulkDischargeSequence.getBlQnt());
		lq.setRefdt(liquidBulkDischargeSequence.getRefdt());
		lq.setProduct(liquidBulkDischargeSequence.getProduct());
		liquidBulkDischargeSequenceRepository.save(lq);
		
	for (PumpingSequence sq : liquidBulkDischargeSequence.getPumpingSequence()) {
		pe.setTerminal(sq.getTerminal());
			pe.setQuantity(sq.getQuantity());
			pe.setRefNo(liquidBulkDischargeSequence.getRefNo());
		pe.setCreatedDate(DateFormatter.getTeSWSLocalDate(LocalDateTime.now()));
		}
		
		pumpingRepository.save(pe);
		
		return response;
	}

}
