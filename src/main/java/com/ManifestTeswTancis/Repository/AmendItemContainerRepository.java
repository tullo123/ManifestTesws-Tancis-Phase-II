package com.ManifestTeswTancis.Repository;

import com.ManifestTeswTancis.Entity.ExImportAmendItemContainer;
import com.ManifestTeswTancis.idEntities.AmendItemContainerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AmendItemContainerRepository extends JpaRepository<ExImportAmendItemContainer, AmendItemContainerId> {

}
