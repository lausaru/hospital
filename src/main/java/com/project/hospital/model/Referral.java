package com.project.hospital.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class Referral {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Date date;
    @OneToOne
    private Specialty specialty;

    public Referral(Specialty specialty) {
        setDate(new Date());
        setSpecialty(specialty);
    }
}
