package com.ManifestTeswTancis.Repository;

import com.ManifestTeswTancis.Entity.BlGoodItemsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlGoodItemsRepository extends JpaRepository<BlGoodItemsEntity,String> {
}
