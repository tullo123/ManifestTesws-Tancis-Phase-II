package com.ManifestTeswTancis.Repository;

import com.ManifestTeswTancis.Entity.CustomClearanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomClearanceRepository extends JpaRepository<CustomClearanceEntity,String > {
    CustomClearanceEntity findFirstByCommunicationAgreedId(String communicationAgreedId);

    Optional<CustomClearanceEntity> findByCommunicationAgreedId(String communicationAgreedId);
}
