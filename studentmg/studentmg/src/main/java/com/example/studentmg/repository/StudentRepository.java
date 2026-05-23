package com.example.studentmg.repository;

import com.example.studentmg.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {

    // how many students have a given status?
    long countByStatus(String status);

    // how many students are in a given year / class?
    long countByYear(String year);
}
