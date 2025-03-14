package com.group2.SPEAR_Backend.Model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "evaluation_submissions")
public class EvaluationSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long submissionId;

    @ManyToOne
    @JoinColumn(name = "evaluation_id", referencedColumnName = "eid", nullable = false)
    private Evaluation evaluation;

    @ManyToOne
    @JoinColumn(name = "evaluator_id", referencedColumnName = "uid", nullable = false)
    private User evaluator;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "is_evaluated", nullable = false)
    private boolean isEvaluated;

    public EvaluationSubmission() {}

    public EvaluationSubmission(Evaluation evaluation, User evaluator, LocalDateTime submittedAt, boolean isEvaluated) {
        this.evaluation = evaluation;
        this.evaluator = evaluator;
        this.submittedAt = submittedAt;
        this.isEvaluated = isEvaluated;
    }

    public Long getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(Long submissionId) {
        this.submissionId = submissionId;
    }

    public Evaluation getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Evaluation evaluation) {
        this.evaluation = evaluation;
    }

    public User getEvaluator() {
        return evaluator;
    }

    public void setEvaluator(User evaluator) {
        this.evaluator = evaluator;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public boolean isEvaluated() {
        return isEvaluated;
    }

    public void setEvaluated(boolean evaluated) {
        isEvaluated = evaluated;
    }
}