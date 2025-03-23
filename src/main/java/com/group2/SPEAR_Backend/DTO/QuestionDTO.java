package com.group2.SPEAR_Backend.DTO;

import com.group2.SPEAR_Backend.Model.Question;
import com.group2.SPEAR_Backend.Model.QuestionType;

public class QuestionDTO {
    private Long qid;
    private String questionText;
    private Long evaluationId;
    private Long classId;
    private QuestionType questionType;
    private Integer createdByUserId;
    private String createdByName;
    private boolean isReuse;

    public QuestionDTO() {
    }

    public QuestionDTO(Long qid, String questionText, Long evaluationId, Long classId, QuestionType questionType, Integer createdByUserId, String createdByName, boolean isReuse) {
        this.qid = qid;
        this.questionText = questionText;
        this.evaluationId = evaluationId;
        this.classId = classId;
        this.questionType = questionType;
        this.createdByUserId = createdByUserId;
        this.createdByName = createdByName;
        this.isReuse = isReuse;
    }

    public QuestionDTO(Question question) {
        this.qid = question.getQid();
        this.questionText = question.getQuestionText();
        this.evaluationId = question.getEvaluation() != null ? question.getEvaluation().getEid() : null;
        this.classId = question.getClasses() != null ? question.getClasses().getCid() : null;
        this.questionType = question.getQuestionType();
        this.createdByUserId = question.getCreatedBy() != null ? question.getCreatedBy().getUid() : null;
        this.createdByName = question.getCreatedBy() != null ? question.getCreatedBy().getFirstname() + " " + question.getCreatedBy().getLastname() : "Unknown";
        this.isReuse = question.isReuse();
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

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public Integer getCreatedByUserId() {
        return createdByUserId;
    }

    public void setCreatedByUserId(Integer createdByUserId) {
        this.createdByUserId = createdByUserId;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public boolean isReuse() {
        return isReuse;
    }

    public void setReuse(boolean reuse) {
        isReuse = reuse;
    }
}
