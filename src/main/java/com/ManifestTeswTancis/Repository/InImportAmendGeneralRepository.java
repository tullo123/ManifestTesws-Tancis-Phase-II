package com.ManifestTeswTancis.Repository;

import com.ManifestTeswTancis.Entity.InImportAmendGeneral;
import com.ManifestTeswTancis.idEntities.InAmendGenId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InImportAmendGeneralRepository extends JpaRepository<InImportAmendGeneral, InAmendGenId> {
    InImportAmendGeneral findByMrn(String mrn);
}
