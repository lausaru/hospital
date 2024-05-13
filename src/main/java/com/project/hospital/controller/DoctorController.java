package com.project.hospital.controller;

import com.project.hospital.Utils;
import com.project.hospital.model.Doctor;
import com.project.hospital.model.Specialty;
import com.project.hospital.repository.DoctorRepository;
import com.project.hospital.repository.SpecialtyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class DoctorController {
    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private SpecialtyRepository specialtyRepository;

    // Create new doctor
    @PostMapping("/doctor")
    public ResponseEntity<String> addNewDoctor(@RequestBody Doctor doctor) {
        String id = Utils.generateDoctorId(doctor.getFullName(),doctorRepository);
        doctor.setId(id);

        // Save doctor in repository and return message
        doctorRepository.save(doctor);

        return ResponseEntity.status(HttpStatus.CREATED).body("Doctor " + doctor.getFullName() + " added with id " + id);
    }

    // Show all doctors
    @GetMapping("/doctors")
    public ResponseEntity<?> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();
        if (doctors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No doctors found.");
        }
        return ResponseEntity.ok(doctors);
    }

    // Show doctor with given id
    @GetMapping("/doctor/{id}")
    public ResponseEntity<?> getDoctorById(@PathVariable(name="id") String id) {
        Optional<Doctor> doctorOptional = doctorRepository.findById(id);
        if (!doctorOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor with id " + id + " not found.");
        }
        return ResponseEntity.ok(doctorOptional.get());
    }

    // Show doctors with given specialty code
    @GetMapping("/doctors/specialty/{code}")
    public ResponseEntity<?> getDoctorsBySpecialty(@PathVariable(name="code") String code) {
        Optional<Specialty> specialtyOptional = specialtyRepository.findByCode(code);
        if (!specialtyOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Specialty with code " + code + " not found.");
        }

        List<Doctor> doctors = doctorRepository.findBySpecialty(code);
        if (doctors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No doctors found with specialty " + specialtyOptional.get().getName() + ".");
        }

        return ResponseEntity.ok(doctors);
    }

    // Delete doctor with given id
    @DeleteMapping("/doctor/{id}")
    public ResponseEntity<?> deleteDoctorById(@PathVariable(name="id") String id) {
        Optional<Doctor> doctorOptional = doctorRepository.findById(id);
        if (!doctorOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor with id " + id + " not found.");
        }

        // If the doctor exists, delete it from the repo
        doctorRepository.delete(doctorOptional.get());

        return ResponseEntity.ok("Doctor "  + doctorOptional.get().getFullName() + ", with id " + id + ", successfully deleted.");
    }
}
