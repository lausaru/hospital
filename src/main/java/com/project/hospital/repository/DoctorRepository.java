package com.project.hospital.repository;

import com.project.hospital.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor,String> {
    List<Doctor> findByIdStartingWith(String initials);
    List<Doctor> findByFullName(String fullName);
}