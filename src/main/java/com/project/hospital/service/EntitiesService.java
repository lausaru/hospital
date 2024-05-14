package com.project.hospital.service;

import com.project.hospital.Utils;
import com.project.hospital.model.*;
import com.project.hospital.repository.*;
import com.project.hospital.security.services.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EntitiesService {
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    SpecialtyRepository specialtyRepository;
    @Autowired
    MedicineRepository medicineRepository;
    @Autowired
    UserService userService;

    // Methods for doctors
    public Doctor addNewDoctor(Doctor doctor) {
        String id = Utils.generateDoctorId(doctor.getFullName(),doctorRepository);
        doctor.setId(id);

        // Save doctor in repository
        doctorRepository.save(doctor);

        // Create new doctor user
        userService.createDoctorUser(doctor);
        return doctor;
    }

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public List<Doctor> findBySpecialty(String code) {
        return doctorRepository.findBySpecialty(code);
    }

    public void deleteDoctor(String id) {
        doctorRepository.deleteById(id);
    }

    // Methods for patients
    public Patient addNewPatient(Patient patient) {
        String id = Utils.generatePatientId(patient.getFullName(),patientRepository);
        patient.setId(id);

        // Save patient in repository
        patientRepository.save(patient);
        return patient;
    }

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public void deletePatient(String id) {
        patientRepository.deleteById(id);
    }


    // Methods for specialties
    public Specialty addNewSpecialty(String specialtyName) {
        String code = Utils.generateSpecialtyCode(specialtyName,specialtyRepository);
        Specialty specialty = new Specialty(code,specialtyName);

        // Save specialty in repository
        specialtyRepository.save(specialty);
        return specialty;
    }

    public List<Specialty> getAllSpecialties() {
        return specialtyRepository.findAll();
    }

    public Specialty updateSpecialty(Specialty specialty, String specialtyName) {
        specialty.setName(specialtyName);

        // Save specialty in repository
        return specialtyRepository.save(specialty);
    }

    // Methods for medicines
    public Medicine addNewMedicine(String medicineName) {
        Medicine medicine = new Medicine(medicineName);

        // Save medicine in repository
        medicineRepository.save(medicine);
        return medicine;
    }

    public List<Medicine> getAllMedicines() {
        return medicineRepository.findAll();
    }

    public Medicine updateMedicine(Medicine medicine, String medicineName) {
        medicine.setName(medicineName);

        // Save medicine in repository
        return medicineRepository.save(medicine);
    }


    // Methods to check if entities exist by the given identifier
    public Doctor doctorWithIdExists(String id) {
        Optional<Doctor> doctorOptional = doctorRepository.findById(id);
        if (doctorOptional.isPresent()) {
            return doctorOptional.get();
        }
        return null;
    }

    public Patient patientWithIdExists(String id) {
        Optional<Patient> patientOptional = patientRepository.findById(id);
        if (patientOptional.isPresent()) {
            return patientOptional.get();
        }
        return null;
    }

    public Specialty specialtyWithCodeExists(String code) {
        Optional<Specialty> specialtyOptional = specialtyRepository.findByCode(code);
        if (specialtyOptional.isPresent()) {
            return specialtyOptional.get();
        }
        return null;
    }

    public Specialty specialtyWithNameExists(String specialtyName) {
        Optional<Specialty> specialtyOptional = specialtyRepository.findByName(specialtyName);
        if (specialtyOptional.isPresent()) {
            return specialtyOptional.get();
        }
        return null;
    }

    public Medicine medicineWithIdExists(int id) {
        Optional<Medicine> medicineOptional = medicineRepository.findById(id);
        if (medicineOptional.isPresent()) {
            return medicineOptional.get();
        }
        return null;
    }

    public Medicine medicineWithNameExists(String medicineName) {
        Optional<Medicine> medicineOptional = medicineRepository.findByName(medicineName);
        if (medicineOptional.isPresent()) {
            return medicineOptional.get();
        }
        return null;
    }

    // Methods for printing info
    public String printDoctorsInfo (List<Doctor> doctors) {
        if (doctors.isEmpty()) {
            return "No doctors found.";
        }

        StringBuilder response = new StringBuilder();
        for (Doctor doctor : doctors) {
            response.append(doctor.printInfo()).append("\n");
        }

        return response.toString();
    }
}
