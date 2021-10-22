package com.ManifestTeswTancis.Repository;

import com.ManifestTeswTancis.Entity.ExImportMasterBl;
import com.ManifestTeswTancis.idEntities.MasterBlId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExImportMasterBlRepository extends JpaRepository<ExImportMasterBl, MasterBlId> {

	List<ExImportMasterBl> findByMrn(String mrn);

	Optional<ExImportMasterBl> findFirstByMrnAndMasterBillOfLading(String mrn, String masterBillOfLading);
}
