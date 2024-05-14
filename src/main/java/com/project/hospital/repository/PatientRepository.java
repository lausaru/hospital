package com.project.hospital.repository;

import com.project.hospital.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient,String> {
    List<Patient> findByIdStartingWith(String initials);
    List<Patient> findByFullName(String fullName);
}