package com.project.hospital.controller;

import com.project.hospital.Utils;
import com.project.hospital.model.Doctor;
import com.project.hospital.model.Patient;
import com.project.hospital.repository.PatientRepository;
import com.project.hospital.service.EntitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class PatientController {
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private EntitiesService entitiesService;

    // Create new patient
    @PostMapping("/patient")
    public ResponseEntity<String> addNewPatient(@RequestBody Patient patient) {
        Patient patientOut = entitiesService.addNewPatient(patient);
        return ResponseEntity.status(HttpStatus.CREATED).body("Patient " + patient.getFullName() + " added with id " + patientOut.getId());
    }

    // Show all patients
    @GetMapping("/patients")
    public ResponseEntity<?> getAllPatients() {
        List<Patient> patients = entitiesService.getAllPatients();
        String out = entitiesService.printInfo(patients);
        if (out.contains("Not found")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(out);
        }
        return ResponseEntity.ok("No patients found.");
    }

    // Show doctor with given id
    @GetMapping("/patient/{id}")
    public ResponseEntity<?> getPatientById(@PathVariable(name="id") String id) {
        Patient patient = entitiesService.patientWithIdExists(id);
        if (patient == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient with id " + id + " not found.");
        }
        return ResponseEntity.ok(patient.printInfo());
    }

    // Delete patient with given id
    @DeleteMapping("/patient/{id}")
    public ResponseEntity<?> deletePatientById(@PathVariable(name="id") String id) {
        Patient patient = entitiesService.patientWithIdExists(id);
        if (patient == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient with id " + id + " not found.");
        }

        entitiesService.deletePatient(id);
        return ResponseEntity.ok("Patient "  + patient.getFullName() + " with id " + id + " successfully deleted.");
    }

}