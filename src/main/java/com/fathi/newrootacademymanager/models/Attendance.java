package com.fathi.newrootacademymanager.models;

import jakarta.persistence.*;

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
    @Column(name = "notes")
    private String notes;

    public Attendance() {}

    public Attendance(int timesPresent, Lesson lesson, Student student) {
        this.timesPresent = timesPresent;
        this.lesson = lesson;
        this.student = student;
    }

    public Attendance(int timesPresent, String notes, Lesson lesson, Student student) {
        this.timesPresent = timesPresent;
        this.notes = notes;
        this.lesson = lesson;
        this.student = student;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
