package com.ManifestTeswTancis.Repository;
import com.ManifestTeswTancis.Entity.LiquidBulkDischargeSequenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LiquidBulkDischargeSequenceRepository extends JpaRepository<LiquidBulkDischargeSequenceEntity, String> {

}
