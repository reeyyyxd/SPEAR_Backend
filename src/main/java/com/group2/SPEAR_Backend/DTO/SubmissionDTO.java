package com.group2.SPEAR_Backend.DTO;

import java.time.LocalDateTime;

public class SubmissionDTO {

    private Long sid;
    private int evaluatorId;
    private Long evaluationId;
    private LocalDateTime submittedAt;
    private String status;

    public SubmissionDTO() {}

    public SubmissionDTO(Long sid, int evaluatorId, Long evaluationId, LocalDateTime submittedAt, String status) {
        this.sid = sid;
        this.evaluatorId = evaluatorId;
        this.evaluationId = evaluationId;
        this.submittedAt = submittedAt;
        this.status = status;
    }

    public Long getSid() {
        return sid;
    }

    public void setSid(Long sid) {
        this.sid = sid;
    }

    public int getEvaluatorId() {
        return evaluatorId;
    }

    public void setEvaluatorId(int evaluatorId) {
        this.evaluatorId = evaluatorId;
    }

    public Long getEvaluationId() {
        return evaluationId;
    }

    public void setEvaluationId(Long evaluationId) {
        this.evaluationId = evaluationId;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
