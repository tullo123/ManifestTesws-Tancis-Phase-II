package com.ManifestTeswTancis.Repository;
import com.ManifestTeswTancis.Entity.LiquidBulkQualityReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface LiquidBulkRepotRepository extends JpaRepository<LiquidBulkQualityReportEntity, String> {

}
