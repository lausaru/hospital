package com.project.hospital.controller;

import com.project.hospital.Utils;
import com.project.hospital.model.Medicine;
import com.project.hospital.model.Patient;
import com.project.hospital.model.Specialty;
import com.project.hospital.repository.MedicineRepository;
import com.project.hospital.repository.SpecialtyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.*;

@RestController
public class HospitalController {

    @Autowired
    private SpecialtyRepository specialtyRepository;
    @Autowired
    private MedicineRepository medicineRepository;

    // Create new specialty
    @PostMapping("/specialty")
    public ResponseEntity<String> addNewSpecialty(@RequestBody String specialtyName) {
        String code = Utils.generateSpecialtyCode(specialtyName,specialtyRepository);
        Specialty specialty = new Specialty(code,specialtyName);

        // Save specialty in repository and return message
        specialtyRepository.save(specialty);

        return ResponseEntity.status(HttpStatus.CREATED).body("Specialty " + specialty.getName() + " added with code " + code);
    }

    // Show all specialties
    @GetMapping("/specialties")
    public ResponseEntity<?> getAllSpecialties() {
        List<Specialty> specialties = specialtyRepository.findAll();
        if (specialties.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No specialties found.");
        }
        return ResponseEntity.ok(specialties);
    }

    // Show specialty by given code
    @GetMapping("/specialty/{code}")
    public ResponseEntity<?> getSpecialtyByCode(@PathVariable(name="code") String code) {
        Optional<Specialty> specialtyOptional = specialtyRepository.findByCode(code);
        if (!specialtyOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Specialty with code " + code + " not found.");
        }
        return ResponseEntity.ok(specialtyOptional.get());
    }

    // Modify specialty name
    @PutMapping("/specialty/{code}")
    public ResponseEntity<?> updateMedicineById(@PathVariable(name="code") String code, @RequestBody String specialtyName) {
        Optional<Specialty> specialtyOptional = specialtyRepository.findByCode(code);
        if (!specialtyOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Specialty with code " + code + " not found.");
        }

        Specialty specialtyUpdated = specialtyOptional.get();
        specialtyUpdated.setName(specialtyName);

        // Save specialty in repository and return message
        specialtyRepository.save(specialtyUpdated);

        return ResponseEntity.status(HttpStatus.OK).body("Specialty name with code " + code + " successfully updated.");
    }

    // Create new medicine
    @PostMapping("/medicine")
    public ResponseEntity<String> addNewMedicine(@RequestBody String medicineName) {
        Optional<Medicine> medicineOptional = medicineRepository.findByName(medicineName);
        if (medicineOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Medicine with name " + medicineOptional.get().getName() + " already exists (with id " + medicineOptional.get().getId() + ").");
        } else {
            Medicine medicine = new Medicine(medicineName);

            // Save medicine in repository and return message
            medicineRepository.save(medicine);

            return ResponseEntity.status(HttpStatus.CREATED).body("Medicine " + medicine.getName() + " added with id " + medicine.getId());
        }
    }

    // Show all medicines
    @GetMapping("/medicines")
    public ResponseEntity<?> getAllMedicines() {
        List<Medicine> medicines = medicineRepository.findAll();
        if (medicines.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No medicines found.");
        }
        return ResponseEntity.ok(medicines);
    }

    // Show medicine by given id
    @GetMapping("/medicine/{id}")
    public ResponseEntity<?> getMedicineById(@PathVariable(name="id") int id) {
        Optional<Medicine> medicineOptional = medicineRepository.findById(id);
        if (!medicineOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Medicine with id " + id + " not found.");
        }
        return ResponseEntity.ok(medicineOptional.get());
    }

    // Modify medicine name
    @PutMapping("/medicine/{id}")
    public ResponseEntity<?> updateMedicineById(@PathVariable(name="id") int id, @RequestBody String medicineName) {
        Optional<Medicine> medicineOptional = medicineRepository.findById(id);
        if (!medicineOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Medicine with id " + id + " not found.");
        }

        Medicine medicineUpdated = medicineOptional.get();
        medicineUpdated.setId(id);
        medicineUpdated.setName(medicineName);

        // Save medicine in repository and return message
        medicineRepository.save(medicineUpdated);

        return ResponseEntity.status(HttpStatus.OK).body("Medicine with id " + medicineUpdated.getId() + " successfully updated.");
    }
}
