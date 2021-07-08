package com.ManifestTeswTancis.Repository;

import com.ManifestTeswTancis.Entity.GoodItemsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlGoodItemsRepository extends JpaRepository<GoodItemsEntity,String> {
}
