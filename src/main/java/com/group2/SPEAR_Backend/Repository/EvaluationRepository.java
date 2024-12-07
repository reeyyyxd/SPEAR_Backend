package com.group2.SPEAR_Backend.Repository;

import com.group2.SPEAR_Backend.Model.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {

    List<Evaluation> findByAvailability(String availability);

    List<Evaluation> findByPeriod(String period);

    List<Evaluation> findByAvailabilityAndPeriod(String availability, String period);

    List<Evaluation> findByClassesCid(Long classId);
}
