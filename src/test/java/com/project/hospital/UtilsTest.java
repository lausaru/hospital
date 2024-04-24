package com.project.hospital;

import com.project.hospital.model.Specialty;
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

    @AfterEach
    void tearDown() {
        specialtyRepository.deleteAll();
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

}