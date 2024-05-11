package com.project.hospital.controller;

import com.project.hospital.Utils;
import com.project.hospital.model.Patient;
import com.project.hospital.repository.PatientRepository;
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

    // Create new patient
    @PostMapping("/patient")
    public ResponseEntity<String> addNewPatient(@RequestBody Patient patient) {
        String id = Utils.generatePatientId(patient.getFullName(),patientRepository);
        patient.setId(id);

        // Save patient in repository and return message
        patientRepository.save(patient);

        return ResponseEntity.status(HttpStatus.CREATED).body("Patient " + patient.getFullName() + " added with id " + id);
    }

    // Show all patients
    @GetMapping("/patients")
    public ResponseEntity<?> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();
        if (patients.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No patients found.");
        }
        return ResponseEntity.ok(patients);
    }

    // Show doctor with given id
    @GetMapping("/patient/{id}")
    public ResponseEntity<?> getPatientById(@PathVariable(name="id") String id) {
        Optional<Patient> patientOptional = patientRepository.findById(id);
        if (!patientOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient with id " + id + " not found.");
        }
        return ResponseEntity.ok(patientOptional.get());
    }


}
