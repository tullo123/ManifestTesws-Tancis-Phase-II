package com.ManifestTeswTancis.Repository;

import com.ManifestTeswTancis.Entity.FreePratiqueReportEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FreePartiqueRepository extends JpaRepository<FreePratiqueReportEntity,String> {
    Optional<FreePratiqueReportEntity> findFirstByCommunicationAgreedId(String communicationAgreedId);
}
