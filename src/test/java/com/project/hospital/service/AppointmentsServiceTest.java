package com.project.hospital.service;

import com.project.hospital.Utils;
import com.project.hospital.model.*;
import com.project.hospital.repository.DoctorRepository;
import com.project.hospital.repository.PatientRepository;
import com.project.hospital.repository.SpecialtyRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AppointmentsServiceTest {
    @Autowired
    AppointmentsService appointmentsService;
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    SpecialtyRepository specialtyRepository;
    @Autowired
    DoctorRepository doctorRepository;

    private final Date date = new Date();

    @AfterEach
    void tearDown() {
        patientRepository.deleteAll();
        doctorRepository.deleteAll();
        specialtyRepository.deleteAll();
    }

    @Test
    void scheduleAppointment_NotMG1Doctors() throws Exception {
        Address judithAddress = new Address("Calle Marina", "Barcelona", "08291");
        Patient patient = new Patient("Judith Peregrina", judithAddress, "699358321", "asf@email.com", BloodType.A);
        patient.setId(Utils.generatePatientId(patient.getFullName(),patientRepository));
        patientRepository.save(patient);

        assertThrows(Exception.class, () -> appointmentsService.scheduleAppointment(patient, date),
                "There are no doctors with the specialty Medicina General.");
    }

    @Test
    void scheduleAppointment_MG1Doctor() throws Exception {
        // Create doctor with specialty MG1
        Address joanAddress = new Address("Calle Balmes", "Valencia", "08291");
        String specialtyName = "Medicina General";
        String specialtyCode = Utils.generateSpecialtyCode(specialtyName,specialtyRepository);
        Specialty medGen = new Specialty(specialtyCode,specialtyName);
        specialtyRepository.save(medGen);

        Doctor doctor = new Doctor("Joan Permanyer", joanAddress, "699358321", "joanp@medical.com", medGen);
        doctor.setId(Utils.generateDoctorId(doctor.getFullName(),doctorRepository));
        doctorRepository.save(doctor);

        // Create patient
        Address judithAddress = new Address("Calle Marina", "Barcelona", "08291");
        Patient patient = new Patient("Judith Peregrina", judithAddress, "699358321", "asf@email.com", BloodType.A);
        patient.setId(Utils.generatePatientId(patient.getFullName(),patientRepository));
        patientRepository.save(patient);

        // Set the appointment
        appointmentsService.scheduleAppointment(patient, date);

        assertEquals(1,patient.getAppointments().size());
        assertEquals(doctor,patient.getAppointments().getFirst().getDoctor());
    }
}