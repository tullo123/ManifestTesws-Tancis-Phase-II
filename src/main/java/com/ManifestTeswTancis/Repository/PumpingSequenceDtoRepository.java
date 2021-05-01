package com.ManifestTeswTancis.Repository;
import com.ManifestTeswTancis.Entity.PumpingSequenceDtoEntity;
import com.ManifestTeswTancis.idEntities.PumpingSequenceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface PumpingSequenceDtoRepository extends JpaRepository<PumpingSequenceDtoEntity, PumpingSequenceId> {

}
