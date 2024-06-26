package com.project.hospital.service;

import com.project.hospital.Utils;
import com.project.hospital.model.*;
import com.project.hospital.repository.AppointmentRepository;
import com.project.hospital.repository.DoctorRepository;
import com.project.hospital.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Random;

@Service
public class AppointmentsService {
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    AppointmentRepository appointmentRepository;

    public String scheduleAppointment(Patient patient, Date date) throws Exception {
        Appointment newAppointment = new Appointment(date, patient);

        // Set the appointment id and doctor
        Doctor doctor = AppointmentsService.setAppointmentsDoctor(newAppointment, doctorRepository);
        newAppointment.setDoctor(doctor);

        // Add the appointment to the patient and to the doctor and the repository
        String id = Utils.generateAppointmentId(patient);
        newAppointment.setId(id);

        patient.addAppointment(newAppointment);
        patientRepository.save(patient);
        doctor.addAppointment(newAppointment);
        doctorRepository.save(doctor);
        appointmentRepository.save(newAppointment);

        return "Appointment scheduled for patient " + patient.getFullName() + " (id " + patient.getId() + ") at date of " + date + " with doctor " + doctor.getFullName() + " (id " + doctor.getId() + ").";
    }

    public static Doctor setAppointmentsDoctor(Appointment appointment, DoctorRepository doctorRepository) throws Exception {
        Patient patient = appointment.getPatient();
        if (patient.getAppointments().isEmpty()) {
            return AppointmentsService.setAppointmentsMedGenDoctor(doctorRepository);
        } else {
            Diagnosis lastDiagnosis = patient.getAppointments().getLast().getDiagnosis();
            if (lastDiagnosis.isReferred()) {
                Specialty specialtyReferral = lastDiagnosis.getReferral().getSpecialty();
                List<Doctor> possibleDoctors = doctorRepository.findBySpecialty(specialtyReferral.getCode());
                if (possibleDoctors.isEmpty()) {
                    throw new Exception("There are no doctors with the specialty to which the patient was referred to in last appointment.");
                } else {
                    // Get random doctor of the list
                    Random rand = new Random();
                    return possibleDoctors.get(rand.nextInt(possibleDoctors.size()));
                }
            } else {
                return AppointmentsService.setAppointmentsMedGenDoctor(doctorRepository);
            }
        }
    }

    public static Doctor setAppointmentsMedGenDoctor(DoctorRepository doctorRepository) throws Exception {
        List<Doctor> doctorsMedGen = doctorRepository.findBySpecialty("MG1");
        if (doctorsMedGen.isEmpty()) {
            throw new Exception("There are no doctors with the specialty Medicina General.");
        } else {
            // Get random doctor of the list
            Random rand = new Random();
            return doctorsMedGen.get(rand.nextInt(doctorsMedGen.size()));
        }
    }

    public boolean isCorrectDate (Date date) {
        // Obtain current date
        Date currentDate = new Date();

        // Check if given date is posterior to current date
        if (date.before(currentDate) || date.equals(currentDate)) {
            return false;
        }

        return true;
    }

    public String printAppointmentsByDoctorId (String doctorId) {
        List<Appointment> appointments = appointmentRepository.findByDoctorId(doctorId);
        if (appointments.isEmpty()) {
            return "Any appointment found for doctor with id " + doctorId;
        }

        StringBuilder response = new StringBuilder();

        for (Appointment appointment : appointments) {
            response.append(appointment.printInfo()).append("\n");
        }

        return response.toString();
    }

    public String printAppointmentsByDoctorIdAndDate (String doctorId, Date date) {
        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndDate(doctorId,date);
        if (appointments.isEmpty()) {
            return "Any appointment found for doctor with id " + doctorId + " at given date.";
        }

        StringBuilder response = new StringBuilder();

        for (Appointment appointment : appointments) {
            response.append(appointment.printInfo()).append("\n");
        }

        return response.toString();
    }
}