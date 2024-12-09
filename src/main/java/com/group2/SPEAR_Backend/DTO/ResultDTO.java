package com.group2.SPEAR_Backend.DTO;

public class ResultDTO {

    private Long resultId;
    private int evaluateeId;
    private Long evaluationId;
    private double averageScore;

    public ResultDTO() {}

    public ResultDTO(Long resultId, int evaluateeId, Long evaluationId, double averageScore) {
        this.resultId = resultId;
        this.evaluateeId = evaluateeId;
        this.evaluationId = evaluationId;
        this.averageScore = averageScore;
    }

    public Long getResultId() {
        return resultId;
    }

    public void setResultId(Long resultId) {
        this.resultId = resultId;
    }

    public int getEvaluateeId() {
        return evaluateeId;
    }

    public void setEvaluateeId(int evaluateeId) {
        this.evaluateeId = evaluateeId;
    }

    public Long getEvaluationId() {
        return evaluationId;
    }

    public void setEvaluationId(Long evaluationId) {
        this.evaluationId = evaluationId;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(double averageScore) {
        this.averageScore = averageScore;
    }
}
