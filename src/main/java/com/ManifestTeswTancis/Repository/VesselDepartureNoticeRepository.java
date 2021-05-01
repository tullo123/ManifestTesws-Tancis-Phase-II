package com.ManifestTeswTancis.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ManifestTeswTancis.Entity.VesselDepartureNoticeEntity;
@Repository
public interface VesselDepartureNoticeRepository extends JpaRepository<VesselDepartureNoticeEntity, String> {

}
