package com.project.hospital.controller;

import com.project.hospital.Utils;
import com.project.hospital.model.Doctor;
import com.project.hospital.model.Medicine;
import com.project.hospital.model.Patient;
import com.project.hospital.model.Specialty;
import com.project.hospital.repository.DoctorRepository;
import com.project.hospital.repository.MedicineRepository;
import com.project.hospital.repository.PatientRepository;
import com.project.hospital.repository.SpecialtyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.Doc;
import java.util.List;

@RestController
public class HospitalController {
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private SpecialtyRepository specialtyRepository;

    @Autowired
    private MedicineRepository medicineRepository;

    @PostMapping("/patient")
    public ResponseEntity<String> addNewPatient(@RequestBody Patient patient) {
        String id = Utils.generatePatientId(patient.getFullName(),patientRepository);
        patient.setId(id);

        // Save patient in repository and return message
        patientRepository.save(patient);

        return ResponseEntity.status(HttpStatus.CREATED).body("Patient " + patient.getFullName() + " added with id " + id);
    }

    @PostMapping("/doctor")
    public ResponseEntity<String> addNewDoctor(@RequestBody Doctor doctor) {
        String id = Utils.generateDoctorId(doctor.getFullName(),doctorRepository);
        doctor.setId(id);

        // Save doctor in repository and return message
        doctorRepository.save(doctor);

        return ResponseEntity.status(HttpStatus.CREATED).body("Doctor " + doctor.getFullName() + " added with id " + id);
    }

    @PostMapping("/specialty")
    public ResponseEntity<String> addNewSpecialty(@RequestBody String specialtyName) {
        String code = Utils.generateSpecialtyCode(specialtyName,specialtyRepository);
        Specialty specialty = new Specialty(code,specialtyName);

        // Save specialty in repository and return message
        specialtyRepository.save(specialty);

        return ResponseEntity.status(HttpStatus.CREATED).body("Specialty " + specialty.getName() + " added with code " + code);
    }

    @PostMapping("/medicine")
    public Medicine addNewMedicine(@RequestBody Medicine medicine) {
        return medicineRepository.save(medicine);
    }

}
