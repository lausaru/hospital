package com.project.hospital.controller;

import com.project.hospital.model.Doctor;
import com.project.hospital.model.Specialty;
import com.project.hospital.service.EntitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DoctorController {
    @Autowired
    private EntitiesService entitiesService;


    // Create new doctor
    @PostMapping("/doctor")
    public ResponseEntity<String> addNewDoctor(@RequestBody Doctor doctor) {
        Specialty doctorSpecialty = entitiesService.specialtyWithCodeExists(doctor.getSpecialty().getCode());
        if (doctorSpecialty == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor cannot be added since specialty with code " + doctor.getSpecialty().getCode() + " does not exist.");
        }

        Doctor doctorOut = entitiesService.addNewDoctor(doctor);
        return ResponseEntity.status(HttpStatus.CREATED).body("Doctor " + doctorOut.getFullName() + " added with id " + doctorOut.getId());
    }

    // Show all doctors
    @GetMapping("/doctors")
    public ResponseEntity<?> getAllDoctors() {
        List<Doctor> doctors = entitiesService.getAllDoctors();
        String out = entitiesService.printInfo(doctors);
        if (out.contains("No doctors")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(out);
        }
        return ResponseEntity.ok(out);
    }

    // Show doctor with given id
    @GetMapping("/doctor/{id}")
    public ResponseEntity<?> getDoctorById(@PathVariable(name="id") String id) {
        Doctor doctor = entitiesService.doctorWithIdExists(id);
        if (doctor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor with id " + id + " not found.");
        }
        return ResponseEntity.ok(doctor.printInfo());
    }

    // Show doctors with given specialty code
    @GetMapping("/doctors/specialty/{code}")
    public ResponseEntity<?> getDoctorsBySpecialty(@PathVariable(name="code") String code) {
        Specialty specialty = entitiesService.specialtyWithCodeExists(code);
        if (specialty == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Specialty with code " + code + " not found.");
        }

        List<Doctor> doctors = entitiesService.getAllDoctors();
        String out = entitiesService.printInfo(doctors);
        if (out.contains("No doctors")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No doctors found with specialty " + specialty.getName() + ".");
        }

        return ResponseEntity.ok(out);
    }

    // Delete doctor with given id
    @DeleteMapping("/doctor/{id}")
    public ResponseEntity<?> deleteDoctorById(@PathVariable(name="id") String id) {
        Doctor doctor = entitiesService.doctorWithIdExists(id);
        if (doctor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor with id " + id + " not found.");
        }

        // If the doctor exists, delete it from the repo
        entitiesService.deleteDoctor(id);

        return ResponseEntity.ok("Doctor "  + doctor.getFullName() + ", with id " + id + ", successfully deleted.");
    }
}
