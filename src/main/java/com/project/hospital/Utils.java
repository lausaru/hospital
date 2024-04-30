package com.project.hospital;

import com.project.hospital.model.Doctor;
import com.project.hospital.model.Patient;
import com.project.hospital.model.Specialty;
import com.project.hospital.repository.DoctorRepository;
import com.project.hospital.repository.PatientRepository;
import com.project.hospital.repository.SpecialtyRepository;

import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

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

    // Method to generate appointments ids
    public static String generateAppointmentId(Patient patient) {
        // Obtain the patient's id
        String patientId = patient.getId();

        // Obtain the number of appointments
        int numAppointments = patient.getAppointments().size();

        // Generate and set id
        return patientId + "-" + numAppointments;
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

    // Method to validate regex patterns (for email, phone and postal code)
    public static boolean patternMatches(String toValidate, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(toValidate)
                .matches();
    }

    // Method to validate email format
    public static boolean isValidEmail(String email) {
        String EMAIL_REGEX =
                "^(?=.{1,256})([a-zA-Z][a-zA-Z0-9._%+-]*@[a-zA-Z0-9-]+(\\.[a-zA-Z]{2,}){1,})$";
        if (email == null) {
            return false;
        }
        return patternMatches(email,EMAIL_REGEX);
    }

    // Method to validate Postal Code format (Spain)
    public static boolean isValidPostalCode(String postalCode) {
        String POSTAL_CODE_REGEX = "^(0[1-9]|[1-4]\\d|5[0-2])\\d{3}$";
        if (postalCode == null) {
            return false;
        }
        return patternMatches(postalCode,POSTAL_CODE_REGEX);
    }

    // Method to validate Phone numbers format (Spain)
    public static boolean isValidSpanishPhoneNumber(String phoneNumber) {
        String PHONE_REGEX = "^(?:(?:\\+|00)34)?(?:6\\d{8}|7\\d{8}|[89]\\d{8})$";
        if (phoneNumber == null) {
            return false;
        }
        return patternMatches(phoneNumber,PHONE_REGEX);
    }
}
