package com.group2.SPEAR_Backend.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "question_templates")
public class QuestionTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "question_text", nullable = false)
    private String questionText;

    @Enumerated(EnumType.STRING)
    @Column(name = "question_type", nullable = false)
    private QuestionType questionType;

    @ManyToOne
    @JoinColumn(name = "template_set_id")
    private QuestionTemplateSet templateSet;

    public QuestionTemplate() {
    }

    public QuestionTemplate(String questionText, QuestionType questionType, QuestionTemplateSet templateSet) {
        this.questionText = questionText;
        this.questionType = questionType;
        this.templateSet = templateSet;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public QuestionTemplateSet getTemplateSet() {
        return templateSet;
    }

    public void setTemplateSet(QuestionTemplateSet templateSet) {
        this.templateSet = templateSet;
    }
}