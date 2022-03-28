package com.ManifestTeswTancis.Repository;

import com.ManifestTeswTancis.Entity.ExLoadingMasterBlEntity;
import com.ManifestTeswTancis.idEntities.ExLoadingMasterBlId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExLoadingMasterBlRepository extends JpaRepository<ExLoadingMasterBlEntity, ExLoadingMasterBlId> {
}
