package com.ManifestTeswTancis.Repository;

import com.ManifestTeswTancis.Entity.EdNoticeEntity;
import com.ManifestTeswTancis.idEntities.EdNoticeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EdNoticeRepository extends JpaRepository<EdNoticeEntity, EdNoticeId> {

    Optional<EdNoticeEntity> findByDocumentNumber(String mrn);
}
