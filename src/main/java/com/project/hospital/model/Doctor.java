package com.project.hospital.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONObject;

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

    public Doctor(String fullName, Address address, String phone, String email, Specialty specialty) {
        super(fullName, address, phone, email);
        setSpecialty(specialty);
    }

    public void addAppointment (Appointment appointment) {
        this.appointments.add(appointment);
    }

    public String printInfo() {
        JSONObject doctorJson = new JSONObject();
        doctorJson.put("\tDoctor id", id);
        doctorJson.put("\tFull name", this.getFullName());
        doctorJson.put("\tStreet address", super.getAddress().getStreetAddress());
        doctorJson.put("\tCity", super.getAddress().getCity());
        doctorJson.put("\tPostal code", super.getAddress().getPostalCode());
        doctorJson.put("\tPhone", this.getPhone());
        doctorJson.put("\tEmail", this.getEmail());
        doctorJson.put("\tSpecialty", this.getSpecialty().getName() + " (code: " + this.getSpecialty().getCode() + ")");

        StringBuilder doctorsInfo = new StringBuilder();
        for (String key : doctorJson.keySet()) {
            doctorsInfo.append(key).append(": ").append(doctorJson.get(key)).append("\n");
        }

        return doctorsInfo.toString();
    }
}