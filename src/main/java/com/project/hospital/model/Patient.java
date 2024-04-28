package com.project.hospital.model;

import jakarta.persistence.*;
import lombok.*;

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
    //@OneToMany
    //private List<Appointment> appointments;


    public Patient(String fullName, Address address, String phone, String email, BloodType bloodType) {
        super(fullName, address, phone, email);
        setBloodType(bloodType);
    }
}