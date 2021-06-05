package com.ManifestTeswTancis.Repository;

import com.ManifestTeswTancis.Entity.InImportManifest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface InImportManifestRepository extends JpaRepository<InImportManifest,String> {

    Optional<InImportManifest> findFirstByCommunicationAgreedId(String communicationAgreedId);
}
