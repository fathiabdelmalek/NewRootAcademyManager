package com.fathi.newrootacademymanager.models;

import jakarta.persistence.*;


@Entity
@Table(name = "incomes_view")
public class IncomeView {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private int amount;
    private String details;
    @Column(name = "time", nullable = false)
    private String time;
    @Column(name = "student_name")
    private String studentName;

    public IncomeView() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}
