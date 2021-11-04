package com.ManifestTeswTancis.Repository;

import com.ManifestTeswTancis.Entity.ExImportBlContainer;
import com.ManifestTeswTancis.idEntities.ContainerId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExImportBlContainerRepository extends JpaRepository<ExImportBlContainer, ContainerId> {
    Optional<ExImportBlContainer> findByMrnAndMasterBillOfLading(String mrn, String masterBillOfLading);

    //Optional<ExImportBlContainer> findByContainerNo(String containerNo);

   // Optional<ExImportBlContainer> findFirstByMrnAndContainerNo(String containerNo);
}
