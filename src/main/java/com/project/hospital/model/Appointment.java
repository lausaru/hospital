package com.project.hospital.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.project.hospital.repository.DoctorRepository;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;
import java.util.Random;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class Appointment {
    @Id
    private String id;
    private boolean closed = false;
    private Date date;
    @ManyToOne
    @JsonBackReference
    private Patient patient;
    @ManyToOne
    private Doctor doctor;
    @OneToOne
    @JoinColumn(name = "diagnosis_id", referencedColumnName = "id")
    private Diagnosis diagnosis;

    public Appointment(Date date, Patient patient) {
        setDate(date);
        setPatient(patient);
        this.diagnosis = null;
    }

    public String printInfo() {
        JSONObject appointmentJson = new JSONObject();
        appointmentJson.put("Appointment id", id);
        appointmentJson.put("Closed", closed);
        appointmentJson.put("Date", date.toString());
        appointmentJson.put("Patient", patient.getFullName() + " (id: " + patient.getId() + ")");
        appointmentJson.put("Doctor", doctor.getFullName() + " (id: " + doctor.getId() + ")");
        appointmentJson.put("Diagnosis", diagnosis != null ? diagnosis.toString() : "null");

        StringBuilder appointmentInfo = new StringBuilder();
        for (String key : appointmentJson.keySet()) {
            appointmentInfo.append(key).append(": ").append(appointmentJson.get(key)).append("\n");
        }

        return appointmentInfo.toString();
    }
}
