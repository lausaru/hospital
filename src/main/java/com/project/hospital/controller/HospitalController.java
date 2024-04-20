package com.project.hospital.controller;

import com.project.hospital.model.Doctor;
import com.project.hospital.model.Patient;
import com.project.hospital.repository.DoctorRepository;
import com.project.hospital.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.print.Doc;
import java.util.List;

@RestController
public class HospitalController {
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @PostMapping("/patient")
    public Patient addNewPatient(@RequestBody Patient patient) {
        return patientRepository.save(patient);
    }

    @PostMapping("/doctor")
    public Doctor addNewDoctor(@RequestBody Doctor doctor) {
        return doctorRepository.save(doctor);
    }

}
