package com.ManifestTeswTancis.Repository;

import com.ManifestTeswTancis.Entity.ExImportAmendGeneral;
import com.ManifestTeswTancis.idEntities.AmendGenId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExImportAmendGeneralRepository extends JpaRepository<ExImportAmendGeneral, AmendGenId> {

    Optional<ExImportAmendGeneral> findFirstByMrnAndAmendSerialNumber(String mrn, String amendSerialNo);
}
