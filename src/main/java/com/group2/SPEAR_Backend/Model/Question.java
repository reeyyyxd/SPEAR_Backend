package com.group2.SPEAR_Backend.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qid;

    @Column(name = "question_text", nullable = false)
    private String questionText;

    @ManyToOne
    @JoinColumn(name = "evaluation_id", referencedColumnName = "eid", nullable = false)
    private Evaluation evaluation;

    @ManyToOne
    @JoinColumn(name = "class_id", referencedColumnName = "cid", nullable = false)
    private Classes classes;

    public Question() {
    }

    public Question(String questionText, Evaluation evaluation, Classes classes) {
        this.questionText = questionText;
        this.evaluation = evaluation;
        this.classes = classes;
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

    public Evaluation getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Evaluation evaluation) {
        this.evaluation = evaluation;
    }

    public Classes getClasses() {
        return classes;
    }

    public void setClasses(Classes classes) {
        this.classes = classes;
    }
}
