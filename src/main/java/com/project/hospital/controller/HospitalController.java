package com.project.hospital.controller;

import com.project.hospital.model.Patient;
import com.project.hospital.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HospitalController {
    @Autowired
    private PatientRepository patientRepository;

    @PostMapping("/patient")
    public Patient addNewPatient(@RequestBody Patient patient) {
        return patientRepository.save(patient);
    }

}
