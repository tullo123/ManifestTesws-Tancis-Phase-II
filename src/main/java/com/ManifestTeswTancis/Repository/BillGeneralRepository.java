package com.ManifestTeswTancis.Repository;

import com.ManifestTeswTancis.Entity.BillGeneralEntity;
import com.ManifestTeswTancis.idEntities.BillGeneralId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BillGeneralRepository extends JpaRepository<BillGeneralEntity, BillGeneralId> {
    Optional<BillGeneralEntity> findFirstByReferenceKeyOneAndReferenceKeyTwoAndReferenceKeyFour(String declarantTin, String amendYear, String amendSerialNo);
}
