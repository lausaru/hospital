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
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.print.Doc;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    @GetMapping("/patients")
    public ResponseEntity<?> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();
        if (patients.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No patients found.");
        }
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/doctors")
    public ResponseEntity<?> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();
        if (doctors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No doctors found.");
        }
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/specialties")
    public ResponseEntity<?> getAllSpecialties() {
        List<Specialty> specialties = specialtyRepository.findAll();
        if (specialties.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No specialties found.");
        }
        return ResponseEntity.ok(specialties);
    }

    @GetMapping("/medicines")
    public ResponseEntity<?> getAllMedicines() {
        List<Medicine> medicines = medicineRepository.findAll();
        if (medicines.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No medicines found.");
        }
        return ResponseEntity.ok(medicines);
    }

}
