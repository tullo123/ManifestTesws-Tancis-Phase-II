package com.ManifestTeswTancis.Repository;

import com.ManifestTeswTancis.Entity.CustomClearanceApprovalStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomClearanceApprovalRepository extends JpaRepository<CustomClearanceApprovalStatus, String> {
    List<CustomClearanceApprovalStatus> findByApprovedStatusFalse();

    List<CustomClearanceApprovalStatus> findFirstByApprovedStatusFalseOrReceivedNoticeSentFalse();

    List<CustomClearanceApprovalStatus> findByApprovedStatusFalseOrReceivedNoticeSentFalse();

    List<CustomClearanceApprovalStatus> findByReceivedNoticeSentFalse();
}
