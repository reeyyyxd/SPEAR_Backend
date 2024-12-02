package com.group2.SPEAR_Backend.DTO;

public class ResponseDTO {

    private Long rid;
    private int evaluatorId;
    private int evaluateeId;
    private Long questionId;
    private double score;

    public ResponseDTO() {}

    public ResponseDTO(Long rid, int evaluatorId, int evaluateeId, Long questionId, double score) {
        this.rid = rid;
        this.evaluatorId = evaluatorId;
        this.evaluateeId = evaluateeId;
        this.questionId = questionId;
        this.score = score;
    }

    public Long getRid() {
        return rid;
    }

    public void setRid(Long rid) {
        this.rid = rid;
    }

    public int getEvaluatorId() {
        return evaluatorId;
    }

    public void setEvaluatorId(int evaluatorId) {
        this.evaluatorId = evaluatorId;
    }

    public int getEvaluateeId() {
        return evaluateeId;
    }

    public void setEvaluateeId(int evaluateeId) {
        this.evaluateeId = evaluateeId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
