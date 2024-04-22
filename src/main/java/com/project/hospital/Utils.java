package com.project.hospital;

import com.project.hospital.model.Specialty;
import com.project.hospital.repository.SpecialtyRepository;

import java.util.List;
import java.util.stream.Collectors;

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
