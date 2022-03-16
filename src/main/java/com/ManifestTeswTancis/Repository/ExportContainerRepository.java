package com.ManifestTeswTancis.Repository;

import com.ManifestTeswTancis.Entity.ExportContainerEntity;
import com.ManifestTeswTancis.idEntities.ExportContainerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExportContainerRepository extends JpaRepository<ExportContainerEntity, ExportContainerId> {
}
