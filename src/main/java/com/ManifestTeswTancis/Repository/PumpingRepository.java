package com.ManifestTeswTancis.Repository;

import com.ManifestTeswTancis.Entity.PumpigSequenceEntity;
import com.ManifestTeswTancis.idEntities.SequenceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface PumpingRepository extends JpaRepository<PumpigSequenceEntity, SequenceId> {

}
