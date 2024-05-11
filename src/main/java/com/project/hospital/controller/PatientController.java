package com.project.hospital.controller;

import com.project.hospital.Utils;
import com.project.hospital.model.Patient;
import com.project.hospital.repository.PatientRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
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

    // Delete patient with given id
    @DeleteMapping("/patient/{id}")
    public ResponseEntity<?> deletePatientById(@PathVariable(name="id") String id) {
        Optional<Patient> patientOptional = patientRepository.findById(id);
        if (!patientOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient with id " + id + " not found.");
        }

        // If the patient exists, delete it from the repo
        patientRepository.delete(patientOptional.get());

        return ResponseEntity.ok("Patient"  + patientOptional.get().getFullName() + " with id " + id + " successfully deleted.");
    }

    // Modify patient data
    @PutMapping("/patient/{id}")
    public ResponseEntity<?> updatePatientById(@PathVariable(name="id") String id, @RequestBody Map<String, Object> updatedPatientInfo) {
        Optional<Patient> patientOptional = patientRepository.findById(id);
        if (!patientOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient with id " + id + " not found.");
        }

        Patient existingPatient = patientOptional.get();

        for (Map.Entry<String, Object> entry : updatedPatientInfo.entrySet()) {
            String propertyName = entry.getKey();
            Object propertyValue = entry.getValue();

            try {
                Field field = Patient.class.getDeclaredField(propertyName);
                field.setAccessible(true);
                field.set(existingPatient, propertyValue);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating patient.");
            }
        }
        patientRepository.save(existingPatient);

        return ResponseEntity.ok("Patient " + existingPatient.getFullName() + ", with id " + id + ", successfully updated.");
    }

}