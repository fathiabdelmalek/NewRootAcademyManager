package com.fathi.newrootacademymanager.models;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "attendances", uniqueConstraints = {@UniqueConstraint(columnNames = {"lesson id", "student id"})})
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "lesson id", nullable = false)
    private Lesson lesson;
    @ManyToOne
    @JoinColumn(name = "student id", nullable = false)
    private Student student;
    @Column(name = "times present", nullable = false)
    private int timesPresent;
    @Column(nullable = false)
    private BigDecimal dues;
    @Column(name = "notes")
    private String notes;

    public Attendance() {
        this.timesPresent = 0;
    }

    public Attendance(Lesson lesson, Student student) {
        this.lesson = lesson;
        this.student = student;
        this.timesPresent = 0;
        this.dues = new BigDecimal("0.00");
    }

    public Attendance(Lesson lesson, Student student, String notes) {
        this.lesson = lesson;
        this.student = student;
        this.timesPresent = 0;
        this.dues = new BigDecimal("0.00");
        this.notes = notes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public int getTimesPresent() {
        return timesPresent;
    }

    public void setTimesPresent(int timesPresent) {
        this.timesPresent = timesPresent;
    }

    public BigDecimal getDues() {
        return dues;
    }

    public void setDues(BigDecimal dues) {
        this.dues = dues;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "student " + student.toString() + " on lesson " + lesson.toString();
    }
}
