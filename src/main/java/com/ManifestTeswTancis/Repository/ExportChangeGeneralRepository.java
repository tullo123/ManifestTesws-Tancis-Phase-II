package com.ManifestTeswTancis.Repository;

import com.ManifestTeswTancis.Entity.ExportChangeGeneralEntity;
import com.ManifestTeswTancis.idEntities.ExportChangeGeneralId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExportChangeGeneralRepository extends JpaRepository<ExportChangeGeneralEntity, ExportChangeGeneralId> {
}
