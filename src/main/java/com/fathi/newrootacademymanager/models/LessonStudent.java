package com.fathi.newrootacademymanager.models;

import jakarta.persistence.*;

@Entity
@Table(name = "lesson student", uniqueConstraints = @UniqueConstraint(columnNames = {"lesson id", "student id"}))
public class LessonStudent {
    @Id
    @ManyToOne
    @JoinColumn(name = "student id", nullable = false)
    private Lesson lesson;
    @Id
    @ManyToOne
    @JoinColumn(name = "student id", nullable = false)
    private Student student;

    public LessonStudent() {}

    public LessonStudent(Lesson lesson, Student student) {
        this.lesson = lesson;
        this.student = student;
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
