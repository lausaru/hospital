package com.project.hospital;

import com.project.hospital.model.Address;
import com.project.hospital.model.BloodType;
import com.project.hospital.model.Patient;
import com.project.hospital.model.Specialty;
import com.project.hospital.repository.PatientRepository;
import com.project.hospital.repository.SpecialtyRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UtilsTest {

    @Autowired
    SpecialtyRepository specialtyRepository;

    @Autowired
    PatientRepository patientRepository;

    @AfterEach
    void tearDown() {
        specialtyRepository.deleteAll();
        patientRepository.deleteAll();
    }

    @Test
    void getInitials() {
        String string = "My name is Marta";
        assertEquals("MNIM",Utils.getInitials(string));
    }

    @Test
    void generateSpecialtyCode() {
        String specialtyName = "General Medicine";
        String code = Utils.generateSpecialtyCode(specialtyName,specialtyRepository);
        assertEquals("GM1",code);
    }

    @Test
    void generateSpecialtyCodeSpecialtyWithInitialAlreadyExisting() {
        String specialtyName1 = "Allergology";
        String code1 = Utils.generateSpecialtyCode(specialtyName1,specialtyRepository);
        assertEquals("A1",code1);

        specialtyRepository.save(new Specialty(code1,specialtyName1));
        String specialtyName2 = "Anesthesiology";
        String code2 = Utils.generateSpecialtyCode(specialtyName2,specialtyRepository);
        assertEquals("A2",code2);

        specialtyRepository.save(new Specialty(code2,specialtyName2));
        String specialtyName3 = "Allergology Paediatrics";
        String code3 = Utils.generateSpecialtyCode(specialtyName3,specialtyRepository);
        assertEquals("AP1",code3);
    }

    @Test
    void generatePatientId() {
        String patientFullName = "Mónica Pradillo";
        String id = Utils.generatePatientId(patientFullName,patientRepository);
        assertEquals("MP1",id);
    }

    @Test
    void generatePatientIdPatientWithInitialAlreadyExisting() {
        String patientFullName1 = "Aurora Gálvez Pardo";
        String id1 = Utils.generatePatientId(patientFullName1,patientRepository);
        assertEquals("AGP1",id1);

        Address address = new Address("Calle Marina", "Barcelona", 5432);
        Patient patient1 = new Patient(patientFullName1,address,654,"email", BloodType.B);
        patient1.setId(id1);
        patientRepository.save(patient1);

        String patientFullName2 = "Aitana Gandia Puente";
        String id2 = Utils.generatePatientId(patientFullName2,patientRepository);
        assertEquals("AGP2",id2);
    }

}