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
        JSONObject appointmentJson = new JSONObject();
        appointmentJson.put("Doctor id", id);
        appointmentJson.put("Full name", this.getFullName());
        appointmentJson.put("Street address", super.getAddress().getStreetAddress());
        appointmentJson.put("City", super.getAddress().getCity());
        appointmentJson.put("Postal code", super.getAddress().getPostalCode());
        appointmentJson.put("Phone", this.getPhone());
        appointmentJson.put("Email", this.getEmail());
        appointmentJson.put("Specialty", this.getSpecialty().getName() + " (code: " + this.getSpecialty().getCode() + ")");

        StringBuilder doctorsInfo = new StringBuilder();
        for (String key : appointmentJson.keySet()) {
            doctorsInfo.append(key).append(": ").append(appointmentJson.get(key)).append("\n");
        }

        return doctorsInfo.toString();
    }
}