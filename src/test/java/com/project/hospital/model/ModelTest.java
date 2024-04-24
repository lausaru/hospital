package com.project.hospital.model;

import com.project.hospital.Utils;
import com.project.hospital.repository.DoctorRepository;
import com.project.hospital.repository.PatientRepository;
import com.project.hospital.repository.SpecialtyRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.print.Doc;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ModelTest {

    @Autowired
    PatientRepository patientRepository;
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    SpecialtyRepository specialtyRepository;

    @BeforeEach
    void setUp() {
        Address judithAddress = new Address("Calle Marina", "Barcelona", 5432);
        Patient patient = new Patient("Judith Peregrina", judithAddress, 452, "email", BloodType.A);
        patient.setId(Utils.generatePatientId(patient.getFullName(),patientRepository));
        patientRepository.save(patient);

        Address joanAddress = new Address("Calle Balmes", "Valencia", 7324);
        String specialtyName = "Medicina General";
        String specialtyCode = Utils.generateSpecialtyCode(specialtyName,specialtyRepository);
        Specialty medGen = new Specialty(specialtyCode,specialtyName);
        specialtyRepository.save(medGen);

        Doctor doctor = new Doctor("Joan Permanyer", joanAddress, 56736, "email", medGen);
        doctor.setId(Utils.generateDoctorId(doctor.getFullName(),doctorRepository));
        doctorRepository.save(doctor);
    }

    @AfterEach
    void tearDown() {
        patientRepository.deleteAll();
        doctorRepository.deleteAll();
        specialtyRepository.deleteAll();
    }

    @Test
    void invalidFullNames() {
        Patient patient = patientRepository.findByFullName("Judith Peregrina").get(0);
        Doctor doctor = doctorRepository.findByFullName("Joan Permanyer").get(0);

        assertThrows(IllegalArgumentException.class, () -> patient.setFullName("Judith"));
        assertThrows(IllegalArgumentException.class, () -> doctor.setFullName("Joan Joan Permanyer Permanyer Peregrina Lopez"));
    }

    @Test
    void validFullNames() {
        Patient patient = patientRepository.findByFullName("Judith Peregrina").get(0);

        patient.setFullName("Judith    Peregrina Pastor");
        assertEquals("Judith Peregrina Pastor", patient.getFullName());
    }


}