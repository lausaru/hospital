package com.project.hospital.model;

import com.project.hospital.repository.DoctorRepository;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
