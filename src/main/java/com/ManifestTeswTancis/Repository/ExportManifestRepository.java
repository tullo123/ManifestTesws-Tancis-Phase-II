package com.ManifestTeswTancis.Repository;

import com.ManifestTeswTancis.Entity.ExportManifest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ExportManifestRepository extends JpaRepository<ExportManifest,String> {

    Optional<ExportManifest> findFirstByCommunicationAgreedId(String communicationAgreedId);

    Optional<ExportManifest> findFirstByMrnOut(String manifestReferenceNumber);
}
