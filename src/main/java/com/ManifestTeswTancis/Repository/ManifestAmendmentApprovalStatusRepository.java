package com.ManifestTeswTancis.Repository;

import com.ManifestTeswTancis.Entity.ManifestAmendmentApprovalStatus;
import com.ManifestTeswTancis.idEntities.ManifestAmendStagingTblId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ManifestAmendmentApprovalStatusRepository extends JpaRepository<ManifestAmendmentApprovalStatus, ManifestAmendStagingTblId> {
    List<ManifestAmendmentApprovalStatus> findByApprovedStatusFalse();

    List<ManifestAmendmentApprovalStatus> findByReceivedNoticeSentFalse();

    List<ManifestAmendmentApprovalStatus> findByPenaltyPaidFalse();

    List<ManifestAmendmentApprovalStatus> findByPenaltyImposedFalse();

    Optional<ManifestAmendmentApprovalStatus> findByAmendReference(String amendmentReference);
}
