package com.project.hospital.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.hospital.Utils;
import com.project.hospital.model.*;
import com.project.hospital.repository.DoctorRepository;
import com.project.hospital.repository.SpecialtyRepository;
import com.project.hospital.service.EntitiesService;
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

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class DoctorControllerTest {
    @Autowired
    WebApplicationContext webApplicationContext;
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    SpecialtyRepository specialtyRepository;
    @Autowired
    EntitiesService entitiesService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    private Date date;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // Create three specialties
        String specialtyName = "Medicina General";
        String specialtyCode = Utils.generateSpecialtyCode(specialtyName,specialtyRepository);
        Specialty medGen = new Specialty(specialtyCode,specialtyName);
        specialtyRepository.save(medGen);

        String specialtyName2 = "Alergología";
        String specialtyCode2 = Utils.generateSpecialtyCode(specialtyName2,specialtyRepository);
        Specialty allergology = new Specialty(specialtyCode2,specialtyName2);
        specialtyRepository.save(allergology);

        String specialtyName3 = "Ginecología";
        String specialtyCode3 = Utils.generateSpecialtyCode(specialtyName3,specialtyRepository);
        Specialty gynecology = new Specialty(specialtyCode3,specialtyName3);
        specialtyRepository.save(gynecology);

        // Create two doctors
        Doctor doctor1 = new Doctor("Marcos Perez", new Address("Calle Marina", "Lugo", "08264"), "657885542", "marc@test.com", medGen);
        doctor1.setId(Utils.generateDoctorId(doctor1.getFullName(),doctorRepository));
        doctorRepository.save(doctor1);

        Doctor doctor2 = new Doctor("Patricia Gomez", new Address("Calle Mayor", "Madrid", "08976"), "790876547", "patricia@test.com", allergology);
        doctor2.setId(Utils.generateDoctorId(doctor2.getFullName(),doctorRepository));
        doctorRepository.save(doctor2);
    }

    @AfterEach
    void tearDown() {
        doctorRepository.deleteAll();
        specialtyRepository.deleteAll();
    }

    @Test
    void addNewDoctor() throws Exception {
        String specialtyName = "Medicina de Familia";
        String specialtyCode = Utils.generateSpecialtyCode(specialtyName,specialtyRepository);
        Specialty medFam = new Specialty(specialtyCode,specialtyName);
        specialtyRepository.save(medFam);

        Doctor fixture = new Doctor("Joan Permanyer", new Address("Calle Balmes", "Valencia", "08291"), "699358321", "email@test.com", medFam);
        fixture.setId(Utils.generateDoctorId(fixture.getFullName(),doctorRepository));
        String body = objectMapper.writeValueAsString(fixture);
        String expectedOutput = "Doctor " + fixture.getFullName() + " added with id " + fixture.getId();

        MvcResult result = mockMvc.perform(post("/doctor")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(content().string(expectedOutput))
                .andReturn();
    }

    @Test
    void getAllDoctors() throws Exception {
        List<Doctor> body = entitiesService.getAllDoctors();

        MvcResult result = mockMvc.perform(get("/doctors")
                        .content(body.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void getDoctorById_NotFound() throws Exception {
        String expectedOutput = "Doctor with id LF1 not found.";

        MvcResult result = mockMvc.perform(get("/doctor/LF1")
                )
                .andExpect(status().isNotFound())
                .andExpect(content().string(expectedOutput))
                .andReturn();
    }

    @Test
    void getDoctorById_OK() throws Exception {
        assertTrue(doctorRepository.findById("MP1").isPresent());

        Doctor body = doctorRepository.findById("MP1").get();

        MvcResult result = mockMvc.perform(get("/doctor/MP1")
                        .content(body.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    void getDoctorBySpecialty_SpecialtyNotFound() throws Exception {
        assertFalse(specialtyRepository.findByCode("SF1").isPresent());

        String expectedOutput = "Specialty with code SF1 not found.";

        MvcResult result = mockMvc.perform(get("/doctors/specialty/SF1")
                )
                .andExpect(status().isNotFound())
                .andExpect(content().string(expectedOutput))
                .andReturn();
    }

    @Test
    void getDoctorBySpecialty_DoctorNotFound() throws Exception {
        assertTrue(specialtyRepository.findByCode("G1").isPresent());
        Specialty specialty = specialtyRepository.findByCode("G1").get();

        String expectedOutput = "No doctors found with specialty " + specialty.getName() + ".";

        MvcResult result = mockMvc.perform(get("/doctors/specialty/G1")
                )
                .andExpect(status().isNotFound())
                .andExpect(content().string(expectedOutput))
                .andReturn();
    }

    @Test
    void getDoctorBySpecialty_OK() throws Exception {
        assertTrue(specialtyRepository.findByCode("MG1").isPresent());
        Specialty specialty = specialtyRepository.findByCode("MG1").get();

        List <Doctor> body = entitiesService.findBySpecialty("MG1");
        assertEquals(1,body.size());

        MvcResult result = mockMvc.perform(get("/doctors/specialty/MG1")
                        .content(body.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
    }

}