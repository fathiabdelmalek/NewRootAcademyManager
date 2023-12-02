package com.fathi.newrootacademymanager.models;

import jakarta.persistence.*;

@Entity
@Table(name = "attendances", uniqueConstraints = @UniqueConstraint(columnNames = {"lesson id", "student id"}))
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "times present", nullable = false)
    private int timesPresent;
    @ManyToOne
    @JoinColumn(name = "lesson id", nullable = false)
    private Lesson lesson;
    @ManyToOne
    @JoinColumn(name = "student id", nullable = false)
    private Student student;

    public Attendance() {}

    public Attendance(int timesPresent, Lesson lesson, Student student) {
        this.timesPresent = timesPresent;
        this.lesson = lesson;
        this.student = student;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTimesPresent() {
        return timesPresent;
    }

    public void setTimesPresent(int timesPresent) {
        this.timesPresent = timesPresent;
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
}
