package com.ManifestTeswTancis.Repository;

import com.ManifestTeswTancis.Entity.ExImportAmendBlContainer;
import com.ManifestTeswTancis.idEntities.ContainerAmendId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExImportAmendBlContainerRepository extends JpaRepository<ExImportAmendBlContainer, ContainerAmendId> {

}
