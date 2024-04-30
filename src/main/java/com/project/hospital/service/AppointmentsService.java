package com.project.hospital.service;

import com.project.hospital.model.Appointment;
import com.project.hospital.model.Patient;
import com.project.hospital.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AppointmentsService {
    @Autowired
    DoctorRepository doctorRepository;

    public void scheduleAppointment(Patient patient, Date date) throws Exception {
        Appointment newAppointment = new Appointment(date, patient);
        newAppointment.setDoctor(patient,doctorRepository);
        patient.addAppointment(newAppointment);
    }
}
