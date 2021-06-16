package com.ManifestTeswTancis.Repository;

import com.ManifestTeswTancis.Entity.VesselDocumentationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VesselDocumentationRepository extends JpaRepository<VesselDocumentationEntity, String> {
    Optional<VesselDocumentationEntity> findFirstByCommunicationAgreedId(String communicationAgreedId);
}
