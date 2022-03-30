package com.ManifestTeswTancis.Repository;

import com.ManifestTeswTancis.Entity.ExImportAmendPenalty;
import com.ManifestTeswTancis.idEntities.AmendPenaltyId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExImportAmendPenaltyRepository extends JpaRepository<ExImportAmendPenalty, AmendPenaltyId> {

    Optional<ExImportAmendPenalty> findFirstByDeclarantTinAndAmendYearAndAmendSerialNumber(String declarantTin, String amendYear, String amendSerialNo);
}
