package com.group2.SPEAR_Backend.Repository;

import com.group2.SPEAR_Backend.Model.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    List<Submission> findByEvaluationEid(Long evaluationId);

    List<Submission> findByEvaluatorUid(int evaluatorId);

    List<Submission> findByStatus(String status);
}
