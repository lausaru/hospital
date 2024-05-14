package com.project.hospital.model;

import com.project.hospital.Utils;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Address {
    private String streetAddress;
    private String city;
    private String postalCode;

    public void setPostalCode(String postalCode) {
        if (Utils.isValidPostalCode(postalCode)) {
            this.postalCode = postalCode;
        } else {
            throw new IllegalArgumentException("Invalid postal code.");
        }
    }

    public void setCity(String city) {
        if (city.matches("^[a-zA-Z]+$")) {
            this.city = city;
        } else {
            throw new IllegalArgumentException("City name should only contain letters.");
        }
    }
}