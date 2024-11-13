//package com.group2.SPEAR_Backend.Model;
//
//import jakarta.persistence.*;
//
//@Entity
//@Table(name = "questions")
//public class Question {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int qid;
//
//    @Column(name = "question")
//    private String question;
//
//    @Column(name = "answer")
//    private String answer;
//
//    @Column(name = "id_evaluator")
//    private int idEvaluator;
//
//    public Question() {
//        super();
//    }
//
//    public Question(int qid, String question, String answer, int idEvaluator) {
//        this.qid = qid;
//        this.question = question;
//        this.answer = answer;
//        this.idEvaluator = idEvaluator;
//    }
//
//    public int getQid() {
//        return qid;
//    }
//
//    public void setQid(int qid) {
//        this.qid = qid;
//    }
//
//    public String getQuestion() {
//        return question;
//    }
//
//    public void setQuestion(String question) {
//        this.question = question;
//    }
//
//    public String getAnswer() {
//        return answer;
//    }
//
//    public void setAnswer(String answer) {
//        this.answer = answer;
//    }
//
//    public int getIdEvaluator() {
//        return idEvaluator;
//    }
//
//    public void setIdEvaluator(int idEvaluator) {
//        this.idEvaluator = idEvaluator;
//    }
//}
