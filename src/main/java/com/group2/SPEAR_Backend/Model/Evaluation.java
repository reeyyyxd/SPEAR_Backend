package com.group2.SPEAR_Backend.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "evaluations")
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eid;

    @JsonIgnore
    @Column(name = "availability", nullable = false)
    private String availability; // "Open" or "Closed"

    @Column(name = "date_open", nullable = false)
    private LocalDate dateOpen;

    @Column(name = "date_close", nullable = false)
    private LocalDate dateClose;

    @Column(name = "period", nullable = false)
    private String period; // Prelim, Midterm, Prefinals, Finals

    @ManyToOne
    @JoinColumn(name = "class_id", referencedColumnName = "cid", nullable = false)
    private Classes classes;


    public Evaluation() {}

    public Evaluation(String availability, LocalDate dateOpen, LocalDate dateClose, String period, Classes classes) {
        this.availability = availability;
        this.dateOpen = dateOpen;
        this.dateClose = dateClose;
        this.period = period;
        this.classes = classes;
    }

    public Long getEid() {
        return eid;
    }

    public void setEid(Long eid) {
        this.eid = eid;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public LocalDate getDateOpen() {
        return dateOpen;
    }

    public void setDateOpen(LocalDate dateOpen) {
        this.dateOpen = dateOpen;
    }

    public LocalDate getDateClose() {
        return dateClose;
    }

    public void setDateClose(LocalDate dateClose) {
        this.dateClose = dateClose;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public Classes getClasses() {
        return classes;
    }

    public void setClasses(Classes classes) {
        this.classes = classes;
    }

}
