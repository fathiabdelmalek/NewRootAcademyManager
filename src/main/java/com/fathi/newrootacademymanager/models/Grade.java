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
    @Column(nullable = false)
    private int year;

    public Grade() {}

    public Grade(Level level, int year) {
        this.level = level;
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @PrePersist
    @PreUpdate
    private void check() {
        if ((level == Level.Primary && (year < 1 || year > 5)) ||
                (level == Level.Middle && (year < 1 || year > 4)) ||
                (level == Level.Secondary && (year < 1 || year > 3))) {
            throw new IllegalArgumentException("Invalid combination of level and year");
        }
    }
}
