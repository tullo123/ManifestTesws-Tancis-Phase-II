package com.ManifestTeswTancis.Repository;

import com.ManifestTeswTancis.Entity.CommonOrdinalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommonOrdinalRepository extends JpaRepository<CommonOrdinalEntity,String> {
    Optional<CommonOrdinalEntity> findByPrefix(String prefix);
}
