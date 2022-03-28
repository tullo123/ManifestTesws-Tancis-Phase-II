package com.ManifestTeswTancis.Repository;

import com.ManifestTeswTancis.Entity.ExportChangeContainerEntity;
import com.ManifestTeswTancis.idEntities.ExportChangeContainerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExportChangeContainerRepository extends JpaRepository<ExportChangeContainerEntity, ExportChangeContainerId> {
}
