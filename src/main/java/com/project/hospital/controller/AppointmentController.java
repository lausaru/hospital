package com.project.hospital.controller;

import com.project.hospital.model.Patient;
import com.project.hospital.repository.PatientRepository;
import com.project.hospital.service.AppointmentsService;
import com.project.hospital.service.EntitiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@RestController
public class AppointmentController {
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private EntitiesService entitiesService;
    @Autowired
    private AppointmentsService appointmentsService;

    @PostMapping("/appointment/{patientId}")
    public ResponseEntity<String> addNewAppointment(@RequestBody String dateString, @PathVariable(name="patientId") String patientId) throws Exception {
        Patient patient = entitiesService.patientWithIdExists(patientId);
        if (patient == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient with id " + patientId + " not found.");
        } else {
            Date date = new Date();
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                date = formatter.parse(dateString);

                if (!appointmentsService.isCorrectDate(date)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The date of the appointment must be later than current date.");
                }

            } catch (ParseException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid date format. Please use dd/MM/yyyy.");
            }

            // Schedule appointment to the given patient and return message
            try {
                String out = appointmentsService.scheduleAppointment(patient, date);
                return ResponseEntity.status(HttpStatus.CREATED).body(out);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
        }
    }
}
