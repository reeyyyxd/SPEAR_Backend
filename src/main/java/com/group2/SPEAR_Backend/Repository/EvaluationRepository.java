package com.group2.SPEAR_Backend.Repository;

import com.group2.SPEAR_Backend.DTO.EvaluationDTO;
import com.group2.SPEAR_Backend.Model.Evaluation;
import com.group2.SPEAR_Backend.Model.EvaluationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {

    // Get evaluations by availability
    List<Evaluation> findByAvailability(String availability);

    // Get evaluations by period (Prelims, Midterms, Prefinals, Finals)
    List<Evaluation> findByPeriod(String period);

    // Get evaluations that are both open and belong to a certain period
    List<Evaluation> findByAvailabilityAndPeriod(String availability, String period);

    // Get evaluations that belong to a specific class
    List<Evaluation> findByClassRef_Cid(Long classId);

    // Get evaluations by their type (STUDENT-TO-STUDENT, STUDENT-TO-ADVISER, ADVISER-TO-STUDENT)
    List<Evaluation> findByEvaluationType(EvaluationType evaluationType);

    // Get all open evaluations (evaluations that are available)
    @Query("SELECT e FROM Evaluation e WHERE e.availability = 'Open'")
    List<Evaluation> findOpenEvaluations();

    // Get evaluations filtered by type and period
    @Query("SELECT e FROM Evaluation e WHERE e.evaluationType = :evaluationType AND e.period = :period")
    List<Evaluation> findByTypeAndPeriod(@Param("evaluationType") EvaluationType evaluationType, @Param("period") String period);

    // Get evaluations with full details (class, period, evaluator names, evaluatee names, team name, and adviser)
    @Query("SELECT new com.group2.SPEAR_Backend.DTO.EvaluationDTO(" +
            "e.eid, e.evaluationType, e.availability, e.dateOpen, e.dateClose, e.period, " +
            "c.cid, c.courseCode, c.section, c.courseDescription, " +
            "t.groupName, " +
            "(SELECT CONCAT(a.firstname, ' ', a.lastname) FROM Team t LEFT JOIN t.adviser a WHERE t.classRef.cid = c.cid), " +
            "(SELECT GROUP_CONCAT(DISTINCT u.firstname || ' ' || u.lastname) FROM EvaluationSubmission es JOIN es.evaluator u WHERE es.evaluation.eid = e.eid), " +
            "(SELECT GROUP_CONCAT(DISTINCT u.firstname || ' ' || u.lastname) FROM Response r JOIN r.evaluatee u WHERE r.evaluation.eid = e.eid), " +
            "(SELECT COUNT(es) > 0 FROM EvaluationSubmission es WHERE es.evaluation.eid = e.eid AND es.isEvaluated = true) " +
            ") FROM Evaluation e " +
            "JOIN e.classRef c " +
            "LEFT JOIN Team t ON t.classRef.cid = c.cid " +
            "WHERE e.eid = :evaluationId")
    EvaluationDTO findEvaluationDetailsWithNames(@Param("evaluationId") Long evaluationId);
}