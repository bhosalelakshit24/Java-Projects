package com.example.studentmg.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String course;        // e.g. BCA, BSc IT
    private String year;          // e.g. First Year, Second Year
    private String status;        // Active / Graduated / Dropped

    private LocalDate joinedDate;

    // Getters & setters
    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getCourse() { return course; }

    public void setCourse(String course) { this.course = course; }

    public String getYear() { return year; }

    public void setYear(String year) { this.year = year; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public LocalDate getJoinedDate() { return joinedDate; }

    public void setJoinedDate(LocalDate joinedDate) { this.joinedDate = joinedDate; }
}
