package com.project.hospital.model;

import com.project.hospital.Utils;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Data {
    private String fullName;
    @Embedded
    private Address address;
    private String phone;
    private String email;

    public Data(String fullName, Address address, String phone, String email) {
        setFullName(fullName);
        setAddress(address);
        setPhone(phone);
        setEmail(email);
    }

    public void setFullName(String fullName) {
        String[] words = fullName.split("\\s+");
        if (words.length >= 2 && words.length <= 5) {
            this.fullName = fullName.replaceAll("\\s+", " ");
        } else {
            throw new IllegalArgumentException("The full name should be composed in between 2 and 5 words.");
        }
    }

    public void setPhone(String phone) {
        String trimmedPhone = phone.replaceAll("\\s+", " ");
        if (Utils.isValidSpanishPhoneNumber(trimmedPhone)) {
            this.phone = trimmedPhone;
        } else {
            throw new IllegalArgumentException("Invalid phone number.");
        }
    }

    public void setEmail(String email) {
        if (Utils.isValidEmail(email)) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("Invalid email.");
        }
    }
}