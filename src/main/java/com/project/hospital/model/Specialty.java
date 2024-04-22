package com.project.hospital.model;

import com.project.hospital.Utils;
import com.project.hospital.repository.SpecialtyRepository;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Specialty {
    @Id
    private String code;
    private String name;

}
