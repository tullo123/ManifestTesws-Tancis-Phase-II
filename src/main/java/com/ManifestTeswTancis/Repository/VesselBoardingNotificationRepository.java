package com.ManifestTeswTancis.Repository;
import org.springframework.stereotype.Repository;
import com.ManifestTeswTancis.Entity.VesselBoardingNotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
@Repository
public interface VesselBoardingNotificationRepository extends JpaRepository<VesselBoardingNotificationEntity, String> {

}
