package com.ManifestTeswTancis.Repository;

import com.ManifestTeswTancis.Entity.CoCompanyCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CoCompanyCodeRepository extends JpaRepository<CoCompanyCodeEntity, String> {

    Optional<CoCompanyCodeEntity> findByCompanyCode(String shippingAgentCode);
}
