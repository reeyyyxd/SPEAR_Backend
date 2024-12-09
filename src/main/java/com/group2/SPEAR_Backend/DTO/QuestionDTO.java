package com.group2.SPEAR_Backend.DTO;

import com.group2.SPEAR_Backend.Model.Question;

public class QuestionDTO {
    private Long qid;
    private String questionText;
    private Long evaluationId;
    private Long classId;

    public QuestionDTO() {
    }

    public QuestionDTO(Long qid, String questionText, Long evaluationId, Long classId) {
        this.qid = qid;
        this.questionText = questionText;
        this.evaluationId = evaluationId;
        this.classId = classId;
    }

    public QuestionDTO(Question question) {
        this.qid = question.getQid();
        this.questionText = question.getQuestionText();
        this.evaluationId = question.getEvaluation() != null ? question.getEvaluation().getEid() : null;
        this.classId = question.getClasses() != null ? question.getClasses().getCid() : null;
    }

    public Long getQid() {
        return qid;
    }

    public void setQid(Long qid) {
        this.qid = qid;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public Long getEvaluationId() {
        return evaluationId;
    }

    public void setEvaluationId(Long evaluationId) {
        this.evaluationId = evaluationId;
    }

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }
}
