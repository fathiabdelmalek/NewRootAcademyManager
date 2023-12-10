package com.fathi.newrootacademymanager.models;

import com.fathi.newrootacademymanager.helpers.enums.WeekDay;
import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
@Table(name = "lessons view")
public class LessonView {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "lesson name", nullable = false)
    private String lessonName;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WeekDay day;
    @Column(name = "start time", nullable = false)
    private LocalTime startTime;
    @Column(name = "end time", nullable = false)
    private LocalTime endTime;
    @Column(nullable = false)
    private float price;
    @Column(name = "classes number", nullable = false, columnDefinition = "DEFAULT (0)")
    private int classesNumber;
    @Column(name = "students number")
    private int studentsNumber;
    @Column(name = "teacher name", nullable = false)
    private String teacherName;
    @Column(name = "room code", nullable = false)
    private String roomCode;
    @Column(nullable = false)
    private String grade;

    public LessonView() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public WeekDay getDay() {
        return day;
    }

    public void setDay(WeekDay day) {
        this.day = day;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public int getClassesNumber() {
        return classesNumber;
    }

    public void setClassesNumber(int classesNumber) {
        this.classesNumber = classesNumber;
    }

    public int getStudentsNumber() {
        return studentsNumber;
    }

    public void setStudentsNumber(int studentsNumber) {
        this.studentsNumber = studentsNumber;
    }
}
