package com.ManifestTeswTancis.Repository;

import com.ManifestTeswTancis.Entity.ExportHouseBlEntity;
import com.ManifestTeswTancis.idEntities.ExportHouseBlId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExportHouseBlRepository extends JpaRepository<ExportHouseBlEntity, ExportHouseBlId> {
}
