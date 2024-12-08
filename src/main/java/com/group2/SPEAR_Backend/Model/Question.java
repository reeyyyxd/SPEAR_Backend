package com.group2.SPEAR_Backend.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qid;

    @Column(name = "question_text", nullable = false)
    private String questionText;

    @Column(name = "question_period", nullable = false)
    private String questionPeriod;

    @ManyToOne
    @JoinColumn(name = "class_id", referencedColumnName = "cid", nullable = false)
    private Classes classes;

    public Question() {
    }

    public Question(String questionText, String questionPeriod, Classes classes) {
        this.questionText = questionText;
        this.questionPeriod = questionPeriod;
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

    public String getQuestionPeriod() {
        return questionPeriod;
    }

    public void setQuestionPeriod(String questionPeriod) {
        this.questionPeriod = questionPeriod;
    }

    public Classes getClasses() {
        return classes;
    }

    public void setClasses(Classes classes) {
        this.classes = classes;
    }
}
