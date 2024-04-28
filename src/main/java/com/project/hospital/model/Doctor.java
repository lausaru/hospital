package com.project.hospital.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Doctor extends Data {
    @Id
    private String id;
    @OneToOne
    @JoinColumn(name = "specialty_code",referencedColumnName = "code")
    private Specialty specialty;

    public Doctor(String fullName, Address address, String phone, String email, Specialty specialty) {
        super(fullName, address, phone, email);
        setSpecialty(specialty);
    }
}