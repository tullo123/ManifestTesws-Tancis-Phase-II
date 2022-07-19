package com.ManifestTeswTancis.Repository;

import com.ManifestTeswTancis.Entity.ManifestApprovalStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ManifestApprovalStatusRepository
		extends JpaRepository<ManifestApprovalStatus, String>{

	List<ManifestApprovalStatus> findByApprovedStatusFalse();

	Optional<ManifestApprovalStatus> findFirstByMrn(String mrn);

    List<ManifestApprovalStatus> findByReceivedNoticeSentFalse();

	List<ManifestApprovalStatus> findByExportManifestApprovedStatusFalse();

	List<ManifestApprovalStatus> findByExportManifestReceivedStatusFalse();
}
