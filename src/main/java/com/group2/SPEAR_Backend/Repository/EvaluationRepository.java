package com.group2.SPEAR_Backend.Repository;

import com.group2.SPEAR_Backend.DTO.EvaluationDTO;
import com.group2.SPEAR_Backend.Model.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {

    List<Evaluation> findByAvailability(String availability);

    List<Evaluation> findByPeriod(String period);

    List<Evaluation> findByAvailabilityAndPeriod(String availability, String period);

    List<Evaluation> findByClassesCid(Long classId);

    @Query("SELECT new com.group2.SPEAR_Backend.DTO.EvaluationDTO(" +
            "e.eid, e.availability, e.dateOpen, e.dateClose, e.period, c.cid, c.courseCode, c.section, c.courseDescription) " +
            "FROM Evaluation e JOIN e.classes c WHERE e.eid = :evaluationId")
    EvaluationDTO findEvaluationDetailsById(@Param("evaluationId") Long evaluationId);

}
