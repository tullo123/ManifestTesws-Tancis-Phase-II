package com.ManifestTeswTancis.Repository;

import com.ManifestTeswTancis.Entity.ExImportAmendItem;
import com.ManifestTeswTancis.idEntities.AmendItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExImportAmendItemRepository extends JpaRepository<ExImportAmendItem, AmendItemId> {

}
