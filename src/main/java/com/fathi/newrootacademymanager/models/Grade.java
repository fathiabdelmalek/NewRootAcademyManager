package com.fathi.newrootacademymanager.models;

import com.fathi.newrootacademymanager.helpers.enums.Level;
import jakarta.persistence.*;

@Entity
@Table(name = "grades")
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Level level;
    @Column(name = "year of grade", nullable = false)
    private int yearOfGrade;

    public Grade() {}

    public Grade(Level level, int yearOfGrade) {
        this.level = level;
        this.yearOfGrade = yearOfGrade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public int getYearOfGrade() {
        return yearOfGrade;
    }

    public void setYearOfGrade(int yearOfGrade) {
        this.yearOfGrade = yearOfGrade;
    }

    @PrePersist
    @PreUpdate
    private void check() {
        if ((level == Level.Primary && (yearOfGrade < 1 || yearOfGrade > 5)) ||
                (level == Level.Middle && (yearOfGrade < 1 || yearOfGrade > 4)) ||
                (level == Level.Secondary && (yearOfGrade < 1 || yearOfGrade > 3))) {
            throw new IllegalArgumentException("Invalid combination of level and year");
        }
    }

    @Override
    public String toString() {
        return level.toString() + " " + yearOfGrade;
    }
}
