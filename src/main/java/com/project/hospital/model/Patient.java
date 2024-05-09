package com.project.hospital.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class Patient extends Data {

    @Id
    private String id;
    @Enumerated(EnumType.STRING)
    private BloodType bloodType;
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments = new ArrayList<>();


    public Patient(String fullName, Address address, String phone, String email, BloodType bloodType) {
        super(fullName, address, phone, email);
        setBloodType(bloodType);
    }

    public void addAppointment (Appointment appointment) {
        this.appointments.add(appointment);
    }
}