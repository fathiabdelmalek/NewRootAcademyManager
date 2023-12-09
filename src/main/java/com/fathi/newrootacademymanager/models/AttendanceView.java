package com.fathi.newrootacademymanager.models;

import jakarta.persistence.*;

@Entity
@Table(name = "attendances view", uniqueConstraints = {@UniqueConstraint(columnNames = {"lesson id", "student id"})})
public class AttendanceView {
    @Id
    @ManyToOne
    @JoinColumn(name = "lesson id", nullable = false)
    private Lesson lesson;
    @Id
    @ManyToOne
    @JoinColumn(name = "student id", nullable = false)
    private Student student;
    @Column(name = "lesson name", nullable = false)
    private String lessonName;
    @Column(name = "student name", nullable = false)
    private String studentName;
    @Column(name = "times present", nullable = false)
    private int timesPresent;
    @Column(name = "notes")
    private String notes;

    public AttendanceView() {}

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
}
