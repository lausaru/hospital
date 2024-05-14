package com.project.hospital.repository;

import com.project.hospital.model.Medicine;
import com.project.hospital.model.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine,Integer> {
    Optional<Medicine> findByName(String name);
}
