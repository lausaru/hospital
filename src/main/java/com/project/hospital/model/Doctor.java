package com.project.hospital.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
//    @OneToMany
//    private List<Appointment> pendingAppointments;
//    @OneToMany
//    private List<Appointment> closedAppointments;

    public Doctor(String fullName, Address address, String phone, String email, Specialty specialty) {
        super(fullName, address, phone, email);
        setSpecialty(specialty);
    }
}