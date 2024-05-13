package com.project.hospital.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class Doctor extends Data {
    @Id
    private String id;
    @OneToOne
    @JoinColumn(name = "specialty_code",referencedColumnName = "code")
    private Specialty specialty;
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments = new ArrayList<>();
//    @OneToMany
//    private List<Appointment> pendingAppointments;
//    @OneToMany
//    private List<Appointment> closedAppointments;

    public Doctor(String fullName, Address address, String phone, String email, Specialty specialty) {
        super(fullName, address, phone, email);
        setSpecialty(specialty);
    }

    public void addAppointment (Appointment appointment) {
        this.appointments.add(appointment);
    }
}