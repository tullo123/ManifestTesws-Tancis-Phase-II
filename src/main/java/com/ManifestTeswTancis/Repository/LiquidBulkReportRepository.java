package com.ManifestTeswTancis.Repository;
import com.ManifestTeswTancis.Entity.LiquidBulkQualityReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LiquidBulkReportRepository extends JpaRepository<LiquidBulkQualityReportEntity, String> {
    Optional<LiquidBulkQualityReportEntity> findByTbsCodeNumber(String tbsCodeNo);
}
