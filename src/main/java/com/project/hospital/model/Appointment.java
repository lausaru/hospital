package com.project.hospital.model;

import com.project.hospital.repository.DoctorRepository;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Random;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Appointment {
    @Id
    private String id;
    private boolean closed = false;
    private Date date;
    @ManyToOne
    private Patient patient;
    @ManyToOne
    private Doctor doctor;
    @OneToOne
    @JoinColumn(name = "diagnosis_id", referencedColumnName = "id")
    private Diagnosis diagnosis;

    public Appointment(Date date, Patient patient) {
        setDate(date);
        setPatient(patient);
    }

    public void setDoctor(Patient patient, DoctorRepository doctorRepository) throws Exception {
        Diagnosis lastDiagnosis = patient.getAppointments().getLast().getDiagnosis();
        if (lastDiagnosis.isReferred()) {
            Specialty specialtyReferral = lastDiagnosis.getReferral().getSpecialty();
            List<Doctor> possibleDoctors = doctorRepository.findBySpecialty(specialtyReferral.getCode());
            if (possibleDoctors.isEmpty()) {
                throw new Exception("There are no doctors with the specialty to which the patient was referred to in last appointment.");
            } else {
                // Get random doctor of the list
                Random rand = new Random();
                this.doctor = possibleDoctors.get(rand.nextInt(possibleDoctors.size()));
            }
        } else {
            List<Doctor> doctorsMedGen = doctorRepository.findBySpecialty("MG1");
            if (doctorsMedGen.isEmpty()) {
                throw new Exception("There are no doctors with the specialty Medicina General.");
            } else {
                // Get random doctor of the list
                Random rand = new Random();
                this.doctor = doctorsMedGen.get(rand.nextInt(doctorsMedGen.size()));
            }
        }
    }
}
