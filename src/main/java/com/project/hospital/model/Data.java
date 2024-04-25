package com.project.hospital.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Data {
    private String fullName;
    @Embedded
    private Address address;
    private int phone;
    private String email;

    public void setFullName(String fullName) {
        String[] words = fullName.split("\\s+");
        if (words.length >= 2 && words.length <= 5) {
            String trimmedFullName = fullName.replaceAll("\\s+", " ");
            this.fullName = trimmedFullName;
        } else {
            throw new IllegalArgumentException("The full name should be composed in between 2 and 5 words.");
        }
    }
}