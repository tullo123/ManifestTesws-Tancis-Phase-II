package com.ManifestTeswTancis.Repository;

import com.ManifestTeswTancis.Entity.ManifestAmendmentApprovalStatus;
import com.ManifestTeswTancis.idEntities.ManifestAmendStagingTblId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManifestAmendmentApprovalStatusRepository extends JpaRepository<ManifestAmendmentApprovalStatus, ManifestAmendStagingTblId> {
}
