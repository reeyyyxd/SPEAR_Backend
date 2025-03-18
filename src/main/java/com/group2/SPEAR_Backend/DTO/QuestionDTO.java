package com.group2.SPEAR_Backend.DTO;

import com.group2.SPEAR_Backend.Model.Question;
import com.group2.SPEAR_Backend.Model.QuestionType;

public class QuestionDTO {
    private Long qid;
    private String questionText;
    private Long evaluationId;
    private Long classId;
    private QuestionType questionType;

    public QuestionDTO() {
    }

    public QuestionDTO(Long qid, String questionText, Long evaluationId, Long classId, QuestionType questionType) {
        this.qid = qid;
        this.questionText = questionText;
        this.evaluationId = evaluationId;
        this.classId = classId;
        this.questionType = questionType;
    }

    public QuestionDTO(Question question) {
        this.qid = question.getQid();
        this.questionText = question.getQuestionText();
        this.evaluationId = question.getEvaluation() != null ? question.getEvaluation().getEid() : null;
        this.classId = question.getClasses() != null ? question.getClasses().getCid() : null;
        this.questionType = question.getQuestionType(); // Ensure Question Type is included
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
}
