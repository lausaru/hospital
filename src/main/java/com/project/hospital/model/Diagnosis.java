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

    public Diagnosis(Specialty specialty, String disease, String observations) {
        setSpecialty(specialty);
        setDisease(disease);
        setObservations(observations);
        this.referred = false;
    }

    public Diagnosis(Specialty specialty, String disease, String observations, Referral referral) {
        setSpecialty(specialty);
        setDisease(disease);
        setObservations(observations);
        setReferral(referral);
        this.referred = true;
    }
}


