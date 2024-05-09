package com.project.hospital.service;

import com.project.hospital.Utils;
import com.project.hospital.model.Doctor;
import com.project.hospital.security.models.User;
import com.project.hospital.security.services.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AccessesService {
    @Autowired
    UserService userService;

    public void createDoctorUser(Doctor doctor) {
        String fullName = doctor.getFullName().toLowerCase();
        String[] nameParts = fullName.split(" ");

        // Username consists of name.surnameID - example: paula.lopezPL1
        String username = nameParts[0] + "." + nameParts[1] + doctor.getId();

        // Password consists of name initials (uppercase), the last 2 digits of the phone and the code of the specialty - example: PL55MG1
        String password = Utils.getInitials(doctor.getFullName()) + doctor.getPhone().substring(doctor.getPhone().length() - 2) + doctor.getSpecialty().getCode();

        // Save the new user with role doctor
        userService.saveUser(new User(null, doctor.getFullName(), username, password, new ArrayList<>()));
        userService.addRoleToUser(username, "ROLE_DOCTOR");
    }
}