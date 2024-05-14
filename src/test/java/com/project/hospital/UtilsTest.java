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
import org.springframework.security.core.parameters.P;

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
        String patientFullName1 = "Aurora Galvez Pardo";
        String id1 = Utils.generatePatientId(patientFullName1,patientRepository);
        assertEquals("AGP1",id1);

        Address address = new Address("Calle Marina", "Barcelona", "08291");
        Patient patient1 = new Patient(patientFullName1,address,"788548831","sf@email.com", BloodType.B);
        patient1.setId(id1);
        patientRepository.save(patient1);

        String patientFullName2 = "Aitana Gandia Puente";
        String id2 = Utils.generatePatientId(patientFullName2,patientRepository);
        assertEquals("AGP2",id2);
    }

    @Test
    void isValidEmail_Valid() {
        assertTrue(Utils.isValidEmail("sf@mail.com"));

        assertTrue(Utils.isValidEmail("sf@mail.uk"));

        assertTrue(Utils.isValidEmail("sf@ma.com"));

        assertTrue(Utils.isValidEmail("sf_@mail.com"));

        assertTrue(Utils.isValidEmail("sf_9@mail.com"));
    }

    @Test
    void isValidEmail_Invalids() {
        assertFalse(Utils.isValidEmail("__4@mail.com")); // Email must begin with a letter
        assertFalse(Utils.isValidEmail("4dxf@mail.com")); // Email must begin with a letter
        assertFalse(Utils.isValidEmail("laurasr@mail.c")); // Email must have at least two letters after the .
        assertFalse(Utils.isValidEmail("laurasr@ma,l.com")); // Email must be only composed of letters after the @ and before the .
    }

    @Test
    void isValidSpanishPostalCode_Valid() {
        // Postal Code must contain 5 digits
        assertTrue(Utils.isValidPostalCode("08291"));
        assertTrue(Utils.isValidPostalCode("25001"));
    }

    @Test
    void isValidSpanishPostalCode_Invalids() {
        // Postal Code must contain 5 digits (and only digits)
        assertFalse(Utils.isValidPostalCode("57109"));
        assertFalse(Utils.isValidPostalCode("0829108291"));
        assertFalse(Utils.isValidPostalCode("4623"));
        assertFalse(Utils.isValidPostalCode("08l91"));
        assertFalse(Utils.isValidPostalCode("08291l"));
    }

    @Test
    void isValidSpanishPhoneNumber_Valid() {
        // Phone number must contain 9 digits, start with 6, 7 or 9 or +34 and contain only numbers (not spacings)
        assertTrue(Utils.isValidSpanishPhoneNumber("677593741"));
        assertTrue(Utils.isValidSpanishPhoneNumber("777593741"));
        assertTrue(Utils.isValidSpanishPhoneNumber("935816352"));
        assertTrue(Utils.isValidSpanishPhoneNumber("+34677854692"));
    }

    @Test
    void isValidSpanishPhoneNumber_Invalids() {
        assertFalse(Utils.isValidSpanishPhoneNumber("6234")); // Phone number must contain 9 digits
        assertFalse(Utils.isValidSpanishPhoneNumber("688l90520")); // Phone number must contain only numbers
        assertFalse(Utils.isValidSpanishPhoneNumber("584392471")); // Phone number must start with 6, 7, 8 or 9
        assertFalse(Utils.isValidSpanishPhoneNumber("677 93 42 84")); // Phone number must not contain spacings
    }

    @Test
    void generateAppointmentId_patientWithoutAppointments() {
        Address judithAddress = new Address("Calle Marina", "Barcelona", "08291");
        Patient patient = new Patient("Judith Peregrina", judithAddress, "699358321", "jp@email.cat", BloodType.A);
        patient.setId(Utils.generatePatientId(patient.getFullName(),patientRepository));
        patientRepository.save(patient);

        assertTrue(Utils.generateAppointmentId(patient).contains("JP1-"));
    }

}