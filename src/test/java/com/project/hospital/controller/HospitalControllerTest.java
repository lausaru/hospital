package com.project.hospital.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.hospital.Utils;
import com.project.hospital.model.*;
import com.project.hospital.repository.DoctorRepository;
import com.project.hospital.repository.MedicineRepository;
import com.project.hospital.repository.PatientRepository;
import com.project.hospital.repository.SpecialtyRepository;
import com.project.hospital.service.AppointmentsService;
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


import java.text.SimpleDateFormat;
import java.util.Date;

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

    @Autowired
    AppointmentsService appointmentsService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    private Date date;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        Address judithAddress = new Address("Calle Marina", "Barcelona", "08291");
        Patient patient = new Patient("Judith Peregrina", judithAddress, "699358321", "gsmf@email.com", BloodType.A);
        patient.setId(Utils.generatePatientId(patient.getFullName(),patientRepository));
        patientRepository.save(patient);

//        Address joanAddress = new Address("Calle Balmes", "Valencia", "08291");
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
        medicineRepository.deleteAll();
    }


    @Test
    void addNewSpecialty() throws Exception {
        String specialtyName = "Anesthesiology";
        String specialtyCode = Utils.generateSpecialtyCode(specialtyName,specialtyRepository);
        Specialty fixture = new Specialty(specialtyCode, specialtyName);

        String expectedOutput = "Specialty " + specialtyName + " added with code " + specialtyCode;

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
        String medicineName = "Ibuprofeno";
        Medicine fixture = new Medicine(medicineName);
        String body = objectMapper.writeValueAsString(fixture);

        MvcResult result = mockMvc.perform(post("/medicine")
                        .content(medicineName)
                        .contentType(MediaType.TEXT_PLAIN)
                )
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        assertTrue(responseBody.contains("Medicine " + fixture.getName() + " added with id "));
    }

    @Test
    void addNewMedicineAlreadyExisting() throws Exception {
        String medicineName = "Ibuprofeno";
        Medicine fixture = new Medicine(medicineName);
        medicineRepository.save(fixture);

        String body = objectMapper.writeValueAsString(fixture);
        String expectedOutput = "Medicine with name " + fixture.getName() + " already exists (with id " + fixture.getId() + ").";

        MvcResult result = mockMvc.perform(post("/medicine")
                        .content(medicineName)
                        .contentType(MediaType.TEXT_PLAIN)
                )
                .andExpect(status().isConflict())
                .andExpect(content().string(expectedOutput))
                .andReturn();
    }

}