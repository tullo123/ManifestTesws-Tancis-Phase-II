package com.ManifestTeswTancis.Repository;

import com.ManifestTeswTancis.Entity.CustomClearanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomClearanceRepository extends JpaRepository<CustomClearanceEntity,String > {
}
