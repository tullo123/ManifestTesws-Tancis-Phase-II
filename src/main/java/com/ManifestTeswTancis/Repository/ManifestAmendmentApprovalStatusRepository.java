package com.ManifestTeswTancis.Repository;

import com.ManifestTeswTancis.Entity.ManifestAmendmentApprovalStatus;
import com.ManifestTeswTancis.idEntities.ManifestAmendStagingTblId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManifestAmendmentApprovalStatusRepository extends JpaRepository<ManifestAmendmentApprovalStatus, ManifestAmendStagingTblId> {
    List<ManifestAmendmentApprovalStatus> findByApprovedStatusFalse();

    List<ManifestAmendmentApprovalStatus> findByReceivedNoticeSentFalse();
}
