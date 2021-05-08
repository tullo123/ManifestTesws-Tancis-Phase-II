package com.ManifestTeswTancis.Repository;

import com.ManifestTeswTancis.Entity.CustomClearanceApprovalStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomClearanceApprovalRepository extends JpaRepository<CustomClearanceApprovalStatus, String> {
    List<CustomClearanceApprovalStatus> findByApprovedStatusFalse();

}
