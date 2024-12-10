package com.group2.SPEAR_Backend.DTO;

public class ResponseDTO {

    private Long rid;
    private int evaluatorId;
    private String evaluatorName;
    private int evaluateeId;
    private String evaluateeName;
    private Long questionId;
    private double score;

    public ResponseDTO() {}

    public ResponseDTO(Long rid, int evaluatorId, String evaluatorName, int evaluateeId, String evaluateeName, Long questionId, double score) {
        this.rid = rid;
        this.evaluatorId = evaluatorId;
        this.evaluatorName = evaluatorName;
        this.evaluateeId = evaluateeId;
        this.evaluateeName = evaluateeName;
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

    public String getEvaluatorName() {
        return evaluatorName;
    }

    public void setEvaluatorName(String evaluatorName) {
        this.evaluatorName = evaluatorName;
    }

    public String getEvaluateeName() {
        return evaluateeName;
    }

    public void setEvaluateeName(String evaluateeName) {
        this.evaluateeName = evaluateeName;
    }
}
