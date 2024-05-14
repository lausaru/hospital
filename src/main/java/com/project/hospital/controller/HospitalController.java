package com.project.hospital.controller;

import com.project.hospital.model.Medicine;
import com.project.hospital.model.Specialty;
import com.project.hospital.repository.MedicineRepository;
import com.project.hospital.repository.SpecialtyRepository;
import com.project.hospital.service.EntitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class HospitalController {

    @Autowired
    private SpecialtyRepository specialtyRepository;
    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private EntitiesService entitiesService;

    // Create new specialty
    @PostMapping("/specialty")
    public ResponseEntity<String> addNewSpecialty(@RequestBody String specialtyName) {
        Specialty specialty = entitiesService.specialtyWithNameExists(specialtyName);
        if (specialty != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Specialty with name " + specialty.getName() + " already exists (with code " + specialty.getCode() + ").");
        } else {
            Specialty specialtyNew = entitiesService.addNewSpecialty(specialtyName);
            return ResponseEntity.status(HttpStatus.CREATED).body("Specialty " + specialtyNew.getName() + " added with code " + specialtyNew.getCode());
        }
    }

    // Show all specialties
    @GetMapping("/specialties")
    public ResponseEntity<?> getAllSpecialties() {
        List<Specialty> specialties = entitiesService.getAllSpecialties();
        if (specialties.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No specialties found.");
        }
        return ResponseEntity.ok(specialties);
    }

    // Show specialty by given code
    @GetMapping("/specialty/{code}")
    public ResponseEntity<?> getSpecialtyByCode(@PathVariable(name="code") String code) {
        Specialty specialty = entitiesService.specialtyWithCodeExists(code);
        if (specialty == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Specialty with code " + code + " not found.");
        }
        return ResponseEntity.ok(specialty);
    }

    // Modify specialty name
    @PutMapping("/specialty/{code}")
    public ResponseEntity<?> updateSpecialtyByCode(@PathVariable(name="code") String code, @RequestBody String specialtyName) {
        Specialty specialty = entitiesService.specialtyWithCodeExists(code);
        if (specialty == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Specialty with code " + code + " not found.");
        }
        Specialty specialtyUpdated = entitiesService.updateSpecialty(specialty,specialtyName);
        return ResponseEntity.status(HttpStatus.OK).body("Specialty name with code " + specialtyUpdated.getCode() + " successfully updated.");
    }

    // Create new medicine
    @PostMapping("/medicine")
    public ResponseEntity<String> addNewMedicine(@RequestBody String medicineName) {
        Medicine medicine = entitiesService.medicineWithNameExists(medicineName);
        if (medicine != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Medicine with name " + medicine.getName() + " already exists (with id " + medicine.getId() + ").");
        } else {
            Medicine medicineNew = entitiesService.addNewMedicine(medicineName);
            return ResponseEntity.status(HttpStatus.CREATED).body("Medicine " + medicineNew.getName() + " added with id " + medicineNew.getId());
        }
    }

    // Show all medicines
    @GetMapping("/medicines")
    public ResponseEntity<?> getAllMedicines() {
        List<Medicine> medicines = entitiesService.getAllMedicines();
        if (medicines.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No medicines found.");
        }
        return ResponseEntity.ok(medicines);
    }

    // Show medicine by given id
    @GetMapping("/medicine/{id}")
    public ResponseEntity<?> getMedicineById(@PathVariable(name="id") int id) {
        Medicine medicine = entitiesService.medicineWithIdExists(id);
        if (medicine == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Medicine with id " + id + " not found.");
        }
        return ResponseEntity.ok(medicine);
    }

    // Modify medicine name
    @PutMapping("/medicine/{id}")
    public ResponseEntity<?> updateMedicineById(@PathVariable(name="id") int id, @RequestBody String medicineName) {
        Medicine medicine = entitiesService.medicineWithIdExists(id);
        if (medicine == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Medicine with id " + id + " not found.");
        }
        Medicine medicineUpdated = entitiesService.updateMedicine(medicine,medicineName);
        return ResponseEntity.status(HttpStatus.OK).body("Medicine with id " + medicineUpdated.getId() + " successfully updated.");
    }
}