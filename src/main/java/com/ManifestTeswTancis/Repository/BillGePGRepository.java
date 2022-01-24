package com.ManifestTeswTancis.Repository;

import com.ManifestTeswTancis.Entity.BillGePGEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BillGePGRepository extends JpaRepository<BillGePGEntity,String> {
    Optional<BillGePGEntity> findByBillSerialNumber(String billSerialNo);
}
