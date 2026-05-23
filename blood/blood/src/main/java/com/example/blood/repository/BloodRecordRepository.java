package com.example.blood.repository;

import com.example.blood.model.BloodRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BloodRecordRepository extends JpaRepository<BloodRecord, Long> {
}
