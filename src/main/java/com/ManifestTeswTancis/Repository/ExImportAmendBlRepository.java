package com.ManifestTeswTancis.Repository;

import com.ManifestTeswTancis.Entity.ExImportAmendBl;
import com.ManifestTeswTancis.idEntities.BlAmendmentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExImportAmendBlRepository extends JpaRepository<ExImportAmendBl, BlAmendmentId> {

}
