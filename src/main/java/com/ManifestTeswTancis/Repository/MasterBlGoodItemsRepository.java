package com.ManifestTeswTancis.Repository;

import com.ManifestTeswTancis.Entity.MasterBlGoodItemsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MasterBlGoodItemsRepository extends JpaRepository<MasterBlGoodItemsEntity,String> {
}
