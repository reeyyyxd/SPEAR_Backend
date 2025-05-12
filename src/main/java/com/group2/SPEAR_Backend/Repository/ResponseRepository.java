package com.group2.SPEAR_Backend.Repository;

import com.group2.SPEAR_Backend.DTO.ResponseDTO;
import com.group2.SPEAR_Backend.Model.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResponseRepository extends JpaRepository<Response, Long> {
    List<Response> findByEvaluatorUid(int evaluatorId);
    List<Response> findByEvaluateeUid(int evaluateeId);

    @Query("SELECT new com.group2.SPEAR_Backend.DTO.ResponseDTO( " +
            "r.rid, " +
            "evaluator.uid, " +
            "CONCAT(evaluator.firstname, ' ', evaluator.lastname), " +
            "evaluatee.uid, " +
            "CONCAT(evaluatee.firstname, ' ', evaluatee.lastname), " +
            "r.question.qid, " +
            "r.score) " +
            "FROM Response r " +
            "JOIN r.evaluator evaluator " +
            "JOIN r.evaluatee evaluatee " +
            "WHERE evaluator.uid = :evaluatorId")
    List<ResponseDTO> findResponsesByEvaluatorId(@Param("evaluatorId") int evaluatorId);

    @Query("SELECT new com.group2.SPEAR_Backend.DTO.ResponseDTO( " +
            "r.rid, " +
            "evaluator.uid, " +
            "CONCAT(evaluator.firstname, ' ', evaluator.lastname), " +
            "evaluatee.uid, " +
            "CONCAT(evaluatee.firstname, ' ', evaluatee.lastname), " +
            "r.question.qid, " +
            "r.score) " +
            "FROM Response r " +
            "JOIN r.evaluator evaluator " +
            "JOIN r.evaluatee evaluatee " +
            "WHERE evaluatee.uid = :evaluateeId")
    List<ResponseDTO> findResponsesByEvaluateeId(@Param("evaluateeId") int evaluateeId);

    @Query("SELECT new com.group2.SPEAR_Backend.DTO.ResponseDTO( " +
            "r.rid, " +
            "evaluator.uid, " +
            "CONCAT(evaluator.firstname, ' ', evaluator.lastname), " +
            "evaluatee.uid, " +
            "CONCAT(evaluatee.firstname, ' ', evaluatee.lastname), " +
            "r.question.qid, " +
            "r.question.questionTitle, " +
            "r.evaluation.period, " +
            "r.score, " +
            "r.textResponse, " +
            "r.question.questionDetails, " +
            "r.question.questionType) " +
            "FROM Response r " +
            "JOIN r.evaluator evaluator " +
            "JOIN r.evaluatee evaluatee " +
            "JOIN r.question question " +
            "JOIN r.evaluation evaluation " +
            "WHERE r.evaluation.eid = :evaluationId")
    List<ResponseDTO> findResponsesByEvaluationId(@Param("evaluationId") Long evaluationId);


    @Modifying
    @Query("DELETE FROM Response r WHERE r.question.qid IN (SELECT q.qid FROM Question q WHERE q.evaluation.eid = :evaluationId)")
    void deleteResponsesByEvaluationId(@Param("evaluationId") Long evaluationId);

    @Modifying
    @Query("""
    DELETE FROM Response r
     WHERE r.evaluator.uid IN (
         SELECT m.uid FROM Team t JOIN t.members m WHERE t.tid = :teamId
       )
       OR r.evaluatee.uid IN (
         SELECT m.uid FROM Team t JOIN t.members m WHERE t.tid = :teamId
       )
""")
    void deleteByTeamMembers(@Param("teamId") int teamId);

    @Modifying
    @Query("""
       DELETE FROM Response r
        WHERE r.evaluator.uid = :studentId
           OR r.evaluatee.uid = :studentId
    """)
    void deleteByParticipant(@Param("studentId") int studentId);









}
