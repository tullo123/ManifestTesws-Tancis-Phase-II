package com.ManifestTeswTancis.Repository;

import com.ManifestTeswTancis.Entity.ExImportMasterBl;
import com.ManifestTeswTancis.idEntities.MasterBlId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExImportMasterBlRepository extends JpaRepository<ExImportMasterBl, MasterBlId> {

	List<ExImportMasterBl> findByMrn(String mrn);

}
