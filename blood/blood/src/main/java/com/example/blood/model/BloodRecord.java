package com.example.blood.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "blood_records")
public class BloodRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String donorName;

    private String bloodGroup;   // A+, A-, B+, O+ etc.

    private Integer units;       // units in ml or bags

    private String city;

    private String hospital;

    private String contactNumber;

    private LocalDate donationDate = LocalDate.now();

    private String status = "AVAILABLE";   // AVAILABLE / USED

    public BloodRecord() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDonorName() { return donorName; }
    public void setDonorName(String donorName) { this.donorName = donorName; }

    public String getBloodGroup() { return bloodGroup; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }

    public Integer getUnits() { return units; }
    public void setUnits(Integer units) { this.units = units; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getHospital() { return hospital; }
    public void setHospital(String hospital) { this.hospital = hospital; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public LocalDate getDonationDate() { return donationDate; }
    public void setDonationDate(LocalDate donationDate) { this.donationDate = donationDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
