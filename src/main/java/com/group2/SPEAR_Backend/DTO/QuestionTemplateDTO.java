package com.group2.SPEAR_Backend.DTO;

import com.group2.SPEAR_Backend.Model.QuestionTemplate;
import com.group2.SPEAR_Backend.Model.QuestionType;

public class QuestionTemplateDTO {
    private Long id;
    private String questionText;
    private QuestionType questionType;
    private Integer createdByUserId;


    public QuestionTemplateDTO() {}

    public QuestionTemplateDTO(QuestionTemplate template) {
        this.id = template.getId();
        this.questionText = template.getQuestionText();
        this.questionType = template.getQuestionType();
        this.createdByUserId = template.getCreatedBy() != null ? template.getCreatedBy().getUid() : null;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }

    public QuestionType getQuestionType() { return questionType; }
    public void setQuestionType(QuestionType questionType) { this.questionType = questionType; }

    public Integer getCreatedByUserId() { return createdByUserId; }
    public void setCreatedByUserId(Integer createdByUserId) { this.createdByUserId = createdByUserId; }

}
