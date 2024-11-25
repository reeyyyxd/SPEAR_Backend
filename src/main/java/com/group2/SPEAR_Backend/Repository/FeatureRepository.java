package com.group2.SPEAR_Backend.Repository;

import com.group2.SPEAR_Backend.Model.Feature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeatureRepository extends JpaRepository<Feature, Integer> {

    @Query("SELECT f FROM Feature f WHERE f.project.pid = :projectId")
    List<Feature> findByProjectId(int projectId);
}
