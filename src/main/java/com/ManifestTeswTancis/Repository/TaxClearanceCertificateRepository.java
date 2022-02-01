package com.ManifestTeswTancis.Repository;

import com.ManifestTeswTancis.Entity.TaxClearanceCertificateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaxClearanceCertificateRepository extends JpaRepository<TaxClearanceCertificateEntity,Integer> {
    Optional<TaxClearanceCertificateEntity> findByTaxCertificateNumber(String taxClearanceNumber);
}
