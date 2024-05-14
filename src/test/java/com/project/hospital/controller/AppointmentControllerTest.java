package com.project.hospital.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.hospital.Utils;
import com.project.hospital.model.*;
import com.project.hospital.repository.DoctorRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class AppointmentControllerTest {
    @Autowired
    WebApplicationContext webApplicationContext;
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    SpecialtyRepository specialtyRepository;

    @Autowired
    AppointmentsService appointmentsService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    private Date date;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @AfterEach
    void tearDown() {
        patientRepository.deleteAll();
        doctorRepository.deleteAll();
        specialtyRepository.deleteAll();
    }

    @Test
    void addNewAppointment_notMG1() throws Exception {
        Patient patient = new Patient("Judith Peregrina", new Address("Calle Marina", "Barcelona", "08291"), "699358321", "email@test.com", BloodType.A);
        patient.setId(Utils.generatePatientId(patient.getFullName(),patientRepository));
        patientRepository.save(patient);

        String dateString = "04/06/2024";
        String expectedOutput = "There are no doctors with the specialty Medicina General.";

        MvcResult result = mockMvc.perform(post("/appointment/JP1")
                        .content(dateString)
                        .contentType(MediaType.TEXT_PLAIN)
                )
                .andExpect(status().isNotFound())
                .andExpect(content().string(expectedOutput))
                .andReturn();
    }

    @Test
    void addNewAppointment_MG1() throws Exception {
        Patient patient = new Patient("Judith Peregrina", new Address("Calle Marina", "Barcelona", "08291"), "699358321", "email@test.com", BloodType.A);
        patient.setId(Utils.generatePatientId(patient.getFullName(),patientRepository));
        patientRepository.save(patient);

        String specialtyName = "Medicina General";
        String specialtyCode = Utils.generateSpecialtyCode(specialtyName,specialtyRepository);
        Specialty medGen = new Specialty(specialtyCode,specialtyName);
        specialtyRepository.save(medGen);

        Doctor doctor = new Doctor("Joan Ledesma", new Address("Calle Balmes", "Valencia", "08291"), "699358321", "email@test.com", medGen);
        doctor.setId(Utils.generateDoctorId(doctor.getFullName(),doctorRepository));
        doctorRepository.save(doctor);

        String dateString = "04/06/2024";
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        date = formatter.parse(dateString);

        String expectedOutput = "Appointment scheduled for patient " + patient.getFullName() + " (id " + patient.getId() + ") at date of " + date + " with doctor " + doctor.getFullName() + " (id " + doctor.getId() + ").";

        MvcResult result = mockMvc.perform(post("/appointment/JP1")
                        .content(dateString)
                        .contentType(MediaType.TEXT_PLAIN)
                )
                .andExpect(status().isCreated())
                .andExpect(content().string(expectedOutput))
                .andReturn();
    }
}
