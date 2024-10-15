package com.group2.SPEAR_Backend.Repository;

import com.group2.SPEAR_Backend.Entity.ClassesEntity;
import com.group2.SPEAR_Backend.Entity.EvaluationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluationRepository extends JpaRepository<EvaluationEntity, Integer> {
}
