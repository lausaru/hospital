package com.project.hospital.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.hospital.Utils;
import com.project.hospital.model.*;
import com.project.hospital.repository.DoctorRepository;
import com.project.hospital.repository.MedicineRepository;
import com.project.hospital.repository.PatientRepository;
import com.project.hospital.repository.SpecialtyRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.print.Doc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class HospitalControllerTest {
    @Autowired
    WebApplicationContext webApplicationContext;
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    SpecialtyRepository specialtyRepository;

    @Autowired
    MedicineRepository medicineRepository;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        Address judithAddress = new Address("Calle Marina", "Barcelona", 5432);
        Patient patient = new Patient("Judith Peregrina", judithAddress, 452, "email", BloodType.A);
        patient.setId(Utils.generatePatientId(patient.getFullName(),patientRepository));
        patientRepository.save(patient);

//        Address joanAddress = new Address("Calle Balmes", "Valencia", 7324);
//        String specialtyName = "Medicina General";
//        String specialtyCode = Utils.generateSpecialtyCode(specialtyName,specialtyRepository);
//        Specialty medGen = new Specialty(specialtyCode,specialtyName);
//        specialtyRepository.save(medGen);
//
//        Doctor doctor = new Doctor("Joan Permanyer", joanAddress, 56736, "email", medGen);
//        doctorRepository.save(doctor);
    }

    @AfterEach
    void tearDown() {
        patientRepository.deleteAll();
        doctorRepository.deleteAll();
        specialtyRepository.deleteAll();
    }

    @Test
    void addNewPatient() throws Exception {
        Patient fixture = new Patient("Judith Peregrina", new Address("Calle Marina", "Barcelona", 5432), 452, "email", BloodType.A);
        fixture.setId(Utils.generatePatientId(fixture.getFullName(),patientRepository));
        String body = objectMapper.writeValueAsString(fixture);

        MvcResult result = mockMvc.perform(post("/patient")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        Patient patient = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                Patient.class
        );

        assertEquals("Judith Peregrina", patient.getFullName());
    }

    @Test
    void addNewDoctor() throws Exception {
        String specialtyName = "Medicina General";
        String specialtyCode = Utils.generateSpecialtyCode(specialtyName,specialtyRepository);
        Specialty medGen = new Specialty(specialtyCode,specialtyName);
        specialtyRepository.save(medGen);

        Doctor fixture = new Doctor("Joan Permanyer", new Address("Calle Balmes", "Valencia", 7324), 56736, "email", medGen);
        fixture.setId(Utils.generateDoctorId(fixture.getFullName(),doctorRepository));
        String body = objectMapper.writeValueAsString(fixture);

        MvcResult result = mockMvc.perform(post("/doctor")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        Doctor doctor = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                Doctor.class
        );

        assertEquals("Joan Permanyer", doctor.getFullName());
    }

    @Test
    void addNewSpecialty() throws Exception {
        String specialtyName = "Anesthesiology";
        String specialtyCode = Utils.generateSpecialtyCode(specialtyName,specialtyRepository);
        Specialty fixture = new Specialty(specialtyCode, specialtyName);

        String expectedOutput = "Specialty " + specialtyName + " added with code: " + specialtyCode;

        MvcResult result = mockMvc.perform(post("/specialty")
                        .content(specialtyName)
                        .contentType(MediaType.TEXT_PLAIN)
                )
                .andExpect(status().isCreated())
                .andExpect(content().string(expectedOutput))
                .andReturn();
    }

    @Test
    void addNewMedicine() throws Exception {
        Medicine fixture = new Medicine("Ibuprofeno");
        String body = objectMapper.writeValueAsString(fixture);

        MvcResult result = mockMvc.perform(post("/medicine")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        Medicine medicine = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                Medicine.class
        );

        assertEquals("Ibuprofeno", medicine.getName());
    }
}