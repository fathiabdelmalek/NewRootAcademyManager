package com.fathi.newrootacademymanager.models;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "attendances view")
public class AttendanceView {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "lesson id", nullable = false)
    private int lesson;
    @Column(name = "student id", nullable = false)
    private int student;
    @Column(name = "lesson name", nullable = false)
    private String lessonName;
    @Column(name = "student name", nullable = false)
    private String studentName;
    @Column(name = "times present", nullable = false)
    private int timesPresent;
    @Column(name = "notes")
    private String notes;
    @Column(nullable = false)
    private BigDecimal dues;

    public AttendanceView() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLesson() {
        return lesson;
    }

    public void setLesson(int lesson) {
        this.lesson = lesson;
    }

    public int getStudent() {
        return student;
    }

    public void setStudent(int student) {
        this.student = student;
    }

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public int getTimesPresent() {
        return timesPresent;
    }

    public void setTimesPresent(int timesPresent) {
        this.timesPresent = timesPresent;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public BigDecimal getDues() {
        return dues;
    }

    public void setDues(BigDecimal dues) {
        this.dues = dues;
    }
}
