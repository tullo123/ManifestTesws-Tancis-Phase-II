package com.ManifestTeswTancis.Repository;

import com.ManifestTeswTancis.Entity.QueueMessageStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QueueMessageStatusRepository extends JpaRepository<QueueMessageStatusEntity, String> {
}
