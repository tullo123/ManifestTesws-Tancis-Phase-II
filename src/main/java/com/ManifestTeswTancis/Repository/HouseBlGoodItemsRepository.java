package com.ManifestTeswTancis.Repository;

import com.ManifestTeswTancis.Entity.HouseBlGoodItemsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HouseBlGoodItemsRepository extends JpaRepository<HouseBlGoodItemsEntity,String> {
}
