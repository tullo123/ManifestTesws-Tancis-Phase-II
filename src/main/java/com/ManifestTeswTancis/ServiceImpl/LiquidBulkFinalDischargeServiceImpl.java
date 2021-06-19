package com.ManifestTeswTancis.ServiceImpl;

import com.ManifestTeswTancis.Entity.LiquidBulkDischargeSequenceEntity;
import com.ManifestTeswTancis.Repository.LiquidBulkDischargeSequenceRepository;
import com.ManifestTeswTancis.Service.LiquidBulkFinalDischargeService;
import com.ManifestTeswTancis.Util.DateFormatter;
import com.ManifestTeswTancis.dtos.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class LiquidBulkFinalDischargeServiceImpl implements LiquidBulkFinalDischargeService {

    final
    LiquidBulkDischargeSequenceRepository liquidBulkDischargeSequenceRepository;

    public LiquidBulkFinalDischargeServiceImpl(LiquidBulkDischargeSequenceRepository liquidBulkDischargeSequenceRepository) {
        this.liquidBulkDischargeSequenceRepository = liquidBulkDischargeSequenceRepository;
    }

    @Override
    public TeswsResponse finalDischargeSequence(LiquidBulkDischargeSequenceUpdateDto liquidBulkDischargeSequenceUpdateDto) {
        TeswsResponse response = new TeswsResponse();
        response.setAckType("LIQUID_BULK_FINAL_DISCHARGE_SEQUENCE");
        response.setRefId(liquidBulkDischargeSequenceUpdateDto.getRefNo());
        response.setCode(200);
        response.setAckDate(DateFormatter.getTeSWSLocalDate(LocalDateTime.now()));
        response.setDescription("Liquid Bulk Final Discharge Sequence Received");

        try{
            Optional<LiquidBulkDischargeSequenceEntity> optional=liquidBulkDischargeSequenceRepository.
                    findFirstByRefNo(liquidBulkDischargeSequenceUpdateDto.getRefNo());
            if(optional.isPresent()){
                LiquidBulkDischargeSequenceEntity liquid=optional.get();
                for(PumpingSequenceDto p:liquidBulkDischargeSequenceUpdateDto.getPumpingSequence()){
                    liquid.setStatus(p.getStatus());
                }
                liquidBulkDischargeSequenceRepository.save(liquid);
            }

        }catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

}
