package com.ManifestTeswTancis.Repository;

import com.ManifestTeswTancis.Entity.ExportCarryInBlEntity;
import com.ManifestTeswTancis.idEntities.ExportCarryInBlId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExportCarryInBlRepository extends JpaRepository<ExportCarryInBlEntity, ExportCarryInBlId> {
}
