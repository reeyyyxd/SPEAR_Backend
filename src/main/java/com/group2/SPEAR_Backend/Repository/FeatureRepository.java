package com.group2.SPEAR_Backend.Repository;

import com.group2.SPEAR_Backend.Entity.FeatureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeatureRepository extends JpaRepository<FeatureEntity, Integer> {

}
