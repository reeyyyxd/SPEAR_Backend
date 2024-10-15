package com.group2.SPEAR_Backend.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "evaluations")
public class EvaluationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int evaluationId;

    @Column(name = "statusForGrading")
    private String statusForGrading;

    @Column(name = "statusForCompletion")
    private String statusForCompletion;

    @Column(name = "userToEvaluate")
    private String userToEvaluate;

    @Column(name = "evaluator")
    private String evaluator;

    public EvaluationEntity() {
        super();
    }

    public EvaluationEntity(int evaluationId, String statusForGrading, String statusForCompletion, String userToEvaluate, String evaluator) {
        this.evaluationId = evaluationId;
        this.statusForGrading = statusForGrading;
        this.statusForCompletion = statusForCompletion;
        this.userToEvaluate = userToEvaluate;
        this.evaluator = evaluator;
    }

    public int getEvaluationId() {
        return evaluationId;
    }

    public void setEvaluationId(int evaluationId) {
        this.evaluationId = evaluationId;
    }

    public String getStatusForGrading() {
        return statusForGrading;
    }

    public void setStatusForGrading(String statusForGrading) {
        this.statusForGrading = statusForGrading;
    }

    public String getStatusForCompletion() {
        return statusForCompletion;
    }

    public void setStatusForCompletion(String statusForCompletion) {
        this.statusForCompletion = statusForCompletion;
    }

    public String getUserToEvaluate() {
        return userToEvaluate;
    }

    public void setUserToEvaluate(String userToEvaluate) {
        this.userToEvaluate = userToEvaluate;
    }

    public String getEvaluator() {
        return evaluator;
    }

    public void setEvaluator(String evaluator) {
        this.evaluator = evaluator;
    }
}
