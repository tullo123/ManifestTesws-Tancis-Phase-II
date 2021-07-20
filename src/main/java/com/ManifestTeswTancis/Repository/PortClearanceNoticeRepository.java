package com.ManifestTeswTancis.Repository;

import com.ManifestTeswTancis.Entity.PortClearanceNoticeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PortClearanceNoticeRepository extends JpaRepository<PortClearanceNoticeEntity, String> {

    Optional<PortClearanceNoticeEntity> findByCommunicationAgreedId(String communicationAgreedId);
}
