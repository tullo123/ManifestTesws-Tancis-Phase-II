package com.ManifestTeswTancis.Repository;

import com.ManifestTeswTancis.Entity.ExImportHouseBl;
import com.ManifestTeswTancis.idEntities.HouseBlId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExImportHouseBlRepository extends JpaRepository<ExImportHouseBl, HouseBlId> {

	List<ExImportHouseBl> findByMrn(String mrn);

    Optional<ExImportHouseBl> findByMrnAndMasterBillOfLadingAndHouseBillOfLading(String mrn, String masterBillOfLading, String houseBillOfLading);
}
