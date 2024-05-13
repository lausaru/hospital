package com.project.hospital.repository;

import com.project.hospital.model.Medicine;
import com.project.hospital.model.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SpecialtyRepository extends JpaRepository<Specialty,String> {
    Optional<Specialty> findByCode(String code);
    Optional<Specialty> findByName(String name);
    List<Specialty> findByCodeStartingWith(String initials);
}
