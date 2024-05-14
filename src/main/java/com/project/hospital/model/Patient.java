package com.project.hospital.model;

import jakarta.persistence.*;
import lombok.*;
import org.json.JSONObject;

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

    public String printInfo() {
        JSONObject patientJson = new JSONObject();
        patientJson.put("\tPatient id", id);
        patientJson.put("\tFull name", this.getFullName());
        patientJson.put("\tStreet address", super.getAddress().getStreetAddress());
        patientJson.put("\tCity", super.getAddress().getCity());
        patientJson.put("\tPostal code", super.getAddress().getPostalCode());
        patientJson.put("\tPhone", this.getPhone());
        patientJson.put("\tEmail", this.getEmail());
        patientJson.put("\tBlood type", this.getBloodType().toString());

        StringBuilder patientsInfo = new StringBuilder();
        for (String key : patientJson.keySet()) {
            patientsInfo.append(key).append(": ").append(patientJson.get(key)).append("\n");
        }

        return patientsInfo.toString();
    }
}