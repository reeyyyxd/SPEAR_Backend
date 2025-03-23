package com.group2.SPEAR_Backend.DTO;

import com.group2.SPEAR_Backend.Model.QuestionTemplate;
import com.group2.SPEAR_Backend.Model.QuestionType;

public class QuestionTemplateDTO {
    private Long id;
    private String questionText;
    private QuestionType questionType;

    public QuestionTemplateDTO() {}

    public QuestionTemplateDTO(QuestionTemplate template) {
        this.id = template.getId();
        this.questionText = template.getQuestionText();
        this.questionType = template.getQuestionType();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }

    public QuestionType getQuestionType() { return questionType; }
    public void setQuestionType(QuestionType questionType) { this.questionType = questionType; }
}