package com.ManifestTeswTancis.Repository;

import com.ManifestTeswTancis.Entity.ExportMasterBlEntity;
import com.ManifestTeswTancis.idEntities.ExportMasterBlId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExportMasterBlRepository extends JpaRepository<ExportMasterBlEntity, ExportMasterBlId> {
}
