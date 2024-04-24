package com.project.hospital;

import com.project.hospital.model.Doctor;
import com.project.hospital.model.Patient;
import com.project.hospital.model.Specialty;
import com.project.hospital.repository.DoctorRepository;
import com.project.hospital.repository.PatientRepository;
import com.project.hospital.repository.SpecialtyRepository;

import java.util.List;

public abstract class Utils {

    // Method to generate specialties codes
    public static String generateSpecialtyCode(String specialtyName, SpecialtyRepository specialtyRepository) {
        // Obtain initial letters from each word of the name
        String initials = Utils.getInitials(specialtyName);

        // Obtain existing specialties with those initials and count
        List<Specialty> existingSpecialties = specialtyRepository.findByCodeStartingWith(initials);
        int nextNumber = existingSpecialties.size() + 1;

        // Generate and set code
        String code = initials + nextNumber;
        return code;
    }

    // Method to generate patients ids
    public static String generatePatientId(String patientFullName, PatientRepository patientRepository) {
        // Obtain initial letters from each word of the name
        String initials = Utils.getInitials(patientFullName);

        // Obtain existing specialties with those initials and count
        List<Patient> existingPatients = patientRepository.findByIdStartingWith(initials);
        int nextNumber = existingPatients.size() + 1;

        // Generate and set id
        String id = initials + nextNumber;
        return id;
    }

    // Method to generate doctors ids
    public static String generateDoctorId(String doctorFullName, DoctorRepository doctorRepository) {
        // Obtain initial letters from each word of the name
        String initials = Utils.getInitials(doctorFullName);

        // Obtain existing specialties with those initials and count
        List<Doctor> existingDoctors = doctorRepository.findByIdStartingWith(initials);
        int nextNumber = existingDoctors.size() + 1;

        // Generate and set id
        String id = initials + nextNumber;
        return id;
    }

    // Method to obtain all the initial letters of each word in a String
    public static String getInitials(String fullString) {
        String[] words = fullString.split("\\s+");
        StringBuilder initials = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                initials.append(word.charAt(0));
            }
        }
        return initials.toString().toUpperCase();
    }
}
