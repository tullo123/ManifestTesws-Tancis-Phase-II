package com.ManifestTeswTancis.Repository;

import com.ManifestTeswTancis.Entity.ExImportManifest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Optional;

@Repository
public interface ExImportManifestRepository extends JpaRepository<ExImportManifest, Serializable> {
	@Query(value="select mrn_id_seq.nextval from dual",nativeQuery = true)
	Object getNextValue();

	Optional<ExImportManifest> findFirstByVoyageNumberAndTransportMeansIdAndCommunicationAgreedId(String voyageNumber,
                                                                                                  String transportMeansId, String communicationAgreedId);
	Optional<ExImportManifest> findFirstByCommunicationAgreedId(String communicationAgreedId);
	
	Optional<ExImportManifest> findFirstByMrn(String mrn);
	
	ExImportManifest findByMrn(String mrn);

}
