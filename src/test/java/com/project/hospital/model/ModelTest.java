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
        Address judithAddress = new Address("Calle Marina", "Barcelona", "08291");
        Patient patient = new Patient("Judith Peregrina", judithAddress, "699358321", "jp@email.cat", BloodType.A);
        patient.setId(Utils.generatePatientId(patient.getFullName(),patientRepository));
        patientRepository.save(patient);

        Address joanAddress = new Address("Calle Balmes", "Valencia", "08291");
        String specialtyName = "Medicina General";
        String specialtyCode = Utils.generateSpecialtyCode(specialtyName,specialtyRepository);
        Specialty medGen = new Specialty(specialtyCode,specialtyName);
        specialtyRepository.save(medGen);

        Doctor doctor = new Doctor("Joan Permanyer", joanAddress, "699358321", "joanp@medical.com", medGen);
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
        Patient patient = patientRepository.findByFullName("Judith Peregrina").getFirst();
        Doctor doctor = doctorRepository.findByFullName("Joan Permanyer").getFirst();

        // fullName only accepts letters and spaces
        assertThrows(IllegalArgumentException.class, () -> {
            try {
                patient.setFullName("Judith Laredo2");
            } catch (IllegalArgumentException e) {
                assertEquals("The full name should only contain letters.", e.getMessage());
                throw e;
            }
        });

        // fullName requires a minimum of 2 words (name and surname)
        assertThrows(IllegalArgumentException.class, () -> {
            try {
                patient.setFullName("Judith");
            } catch (IllegalArgumentException e) {
                assertEquals("The full name should be composed in between 2 and 5 words.", e.getMessage());
                throw e;
            }
        });

        // fullName accepts a maximum of 5 words
        assertThrows(IllegalArgumentException.class, () -> {
            try {
                doctor.setFullName("Joan Joan Permanyer Permanyer Peregrina Lopez");
            } catch (IllegalArgumentException e) {
                assertEquals("The full name should be composed in between 2 and 5 words.", e.getMessage());
                throw e;
            }
        });
    }

    @Test
    void validFullNames() {
        Patient patient = patientRepository.findByFullName("Judith Peregrina").getFirst();

        // fullName should exclude extra spacings for the validation (between 2 and 5 words), and remove them
        patient.setFullName("Judith    Peregrina Pastor");
        assertEquals("Judith Peregrina Pastor", patient.getFullName());
    }

    @Test
    void invalidEmails() {
        Patient patient = patientRepository.findByFullName("Judith Peregrina").getFirst();
        Doctor doctor = doctorRepository.findByFullName("Joan Permanyer").getFirst();

        // same invalid emails tested in UtilsTest
        assertThrows(IllegalArgumentException.class, () -> patient.setEmail("__4@mail.com"));
        assertThrows(IllegalArgumentException.class, () -> patient.setEmail("judper@mail.c"));

        assertThrows(IllegalArgumentException.class, () -> doctor.setEmail("4dxf@mail.com"));
        assertThrows(IllegalArgumentException.class, () -> doctor.setEmail("joanpe@ma,l.com"));
    }

    @Test
    void validEmails() {
        Patient patient = patientRepository.findByFullName("Judith Peregrina").getFirst();
        Doctor doctor = doctorRepository.findByFullName("Joan Permanyer").getFirst();

        patient.setEmail("sf@mail.com");
        assertEquals("sf@mail.com",patient.getEmail());

        doctor.setEmail("sf_9@mail.com");
        assertEquals("sf_9@mail.com",doctor.getEmail());
    }

    @Test
    void invalidPostalCodes() {
        Address patientAddress = patientRepository.findByFullName("Judith Peregrina").getFirst().getAddress();
        Address doctorAddress = doctorRepository.findByFullName("Joan Permanyer").getFirst().getAddress();

        assertThrows(IllegalArgumentException.class, () -> patientAddress.setPostalCode("78423"));

        assertThrows(IllegalArgumentException.class, () -> doctorAddress.setPostalCode("082"));
    }

    @Test
    void validPostalCodes() {
        Address patientAddress = patientRepository.findByFullName("Judith Peregrina").getFirst().getAddress();
        Address doctorAddress = doctorRepository.findByFullName("Joan Permanyer").getFirst().getAddress();

        patientAddress.setPostalCode("08291");
        assertEquals("08291",patientAddress.getPostalCode());

        doctorAddress.setPostalCode("25001");
        assertEquals("25001",doctorAddress.getPostalCode());
    }

    @Test
    void invalidCity() {
        Address patientAddress = patientRepository.findByFullName("Judith Peregrina").getFirst().getAddress();
        Address doctorAddress = doctorRepository.findByFullName("Joan Permanyer").getFirst().getAddress();

        assertThrows(IllegalArgumentException.class, () -> patientAddress.setCity("Barcel0na"));

        assertThrows(IllegalArgumentException.class, () -> doctorAddress.setCity("Barcelona!"));
    }

    @Test
    void validCity() {
        Address patientAddress = patientRepository.findByFullName("Judith Peregrina").getFirst().getAddress();
        Address doctorAddress = doctorRepository.findByFullName("Joan Permanyer").getFirst().getAddress();

        patientAddress.setCity("Barcelona");
        doctorAddress.setCity("Valencia");

        assertEquals("Barcelona",patientAddress.getCity());
        assertEquals("Valencia",doctorAddress.getCity());
    }

    @Test
    void invalidPhones() {
        Patient patient = patientRepository.findByFullName("Judith Peregrina").getFirst();
        Doctor doctor = doctorRepository.findByFullName("Joan Permanyer").getFirst();

        assertThrows(IllegalArgumentException.class, () -> patient.setPhone("08355"));

        assertThrows(IllegalArgumentException.class, () -> doctor.setPhone("+35788566483"));
    }

    @Test
    void validPhones() {
        Patient patient = patientRepository.findByFullName("Judith Peregrina").getFirst();
        Doctor doctor = doctorRepository.findByFullName("Joan Permanyer").getFirst();

        patient.setPhone("777593741");
        assertEquals("777593741",patient.getPhone());

        doctor.setPhone("937843211");
        assertEquals("937843211",doctor.getPhone());
    }

}