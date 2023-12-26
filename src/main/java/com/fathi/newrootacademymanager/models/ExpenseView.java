package com.fathi.newrootacademymanager.models;

import jakarta.persistence.*;


@Entity
@Table(name = "expenses_view")
public class ExpenseView {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private int amount;
    private String details;
    @Column(name = "time", nullable = false)
    private String time;
    @Column(name = "teacher_name")
    private String teacherName;

    public ExpenseView() {
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

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}
