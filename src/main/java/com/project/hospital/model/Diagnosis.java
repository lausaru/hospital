package com.project.hospital.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Diagnosis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne
    private Specialty specialty;
    private String disease;
    private String observations;
    @OneToOne
    private Referral referral;
    private boolean referred;
    @OneToOne
    private Medicine medicine;

    public Diagnosis(Specialty specialty, String disease, String observations, Medicine medicine) {
        setSpecialty(specialty);
        setDisease(disease);
        setObservations(observations);
        setMedicine(medicine);
        this.referred = false;
    }

    public Diagnosis(Specialty specialty, String disease, String observations, Medicine medicine, Referral referral) {
        setSpecialty(specialty);
        setDisease(disease);
        setObservations(observations);
        setReferral(referral);
        setMedicine(medicine);
        this.referred = true;
    }
}


