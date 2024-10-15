package com.group2.SPEAR_Backend.Repository;

import com.group2.SPEAR_Backend.Entity.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<QuestionEntity, Integer> {
}
