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

// useful in the future for more detailing
//    @Column(name = "question_period", nullable = false)
//    private int questionPeriod;

    @ManyToOne
    @JoinColumn(name = "class_id", referencedColumnName = "cid", nullable = false)
    private Classes classes;


    public Question() {}

    public Question(String questionText, Classes classes) {
        this.questionText = questionText;
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

    public Classes getClasses() {
        return classes;
    }

    public void setClasses(Classes classes) {
        this.classes = classes;
    }

//    public int getQuestionPeriod() {
//        return questionPeriod;
//    }
//
//    public void setQuestionPeriod(int questionPeriod) {
//        this.questionPeriod = questionPeriod;
//    }
}
