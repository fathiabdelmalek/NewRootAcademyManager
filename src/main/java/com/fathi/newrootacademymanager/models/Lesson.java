package com.fathi.newrootacademymanager.models;

import com.fathi.newrootacademymanager.helpers.enums.WeekDay;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalTime;

@Entity
@Table(name = "lessons")
public class Lesson {
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
    private BigDecimal price;
    @Column(name = "classes number", nullable = false, columnDefinition = "DEFAULT (0)")
    private int classesNumber;
    @Column(name = "teacher dues", nullable = false, columnDefinition = "DEFAULT (0.00)")
    private BigDecimal teacherDues;
    @ManyToOne
    @JoinColumn(name = "teacher id", nullable = false)
    private Teacher teacher;
    @ManyToOne
    @JoinColumn(name = "room id", nullable = false)
    private Room room;
    @ManyToOne
    @JoinColumn(name = "grade id")
    private Grade grade;

    public Lesson() {
        this.teacherDues = new BigDecimal("0.00");
    }

    public Lesson(String lessonName, WeekDay day, LocalTime startTime, LocalTime endTime, BigDecimal price, Teacher teacher, Room room) {
        this.lessonName = lessonName;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
        this.teacherDues = new BigDecimal("0.00");
        this.teacher = teacher;
        this.room = room;
    }

    public Lesson(String lessonName, WeekDay day, LocalTime startTime, LocalTime endTime, BigDecimal price, Teacher teacher, Room room, Grade grade) {
        this.lessonName = lessonName;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
        this.teacherDues = new BigDecimal("0.00");
        this.teacher = teacher;
        this.room = room;
        this.grade = grade;
    }

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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getClassesNumber() {
        return classesNumber;
    }

    public void setClassesNumber(int classesNumber) {
        this.classesNumber = classesNumber;
    }

    public BigDecimal getTeacherDues() {
        return teacherDues;
    }

    public void setTeacherDues(BigDecimal teacherDues) {
        this.teacherDues = teacherDues;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }
}
