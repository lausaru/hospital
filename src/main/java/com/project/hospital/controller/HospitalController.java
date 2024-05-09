package com.project.hospital.controller;

import com.project.hospital.Utils;
import com.project.hospital.model.*;
import com.project.hospital.repository.DoctorRepository;
import com.project.hospital.repository.MedicineRepository;
import com.project.hospital.repository.PatientRepository;
import com.project.hospital.repository.SpecialtyRepository;
import com.project.hospital.service.AppointmentsService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.Doc;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class HospitalController {
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private SpecialtyRepository specialtyRepository;

    @Autowired
    private MedicineRepository medicineRepository;

    @Autowired
    private AppointmentsService appointmentsService;

    @PostMapping("/patient")
    public ResponseEntity<String> addNewPatient(@RequestBody Patient patient) {
        String id = Utils.generatePatientId(patient.getFullName(),patientRepository);
        patient.setId(id);

        // Save patient in repository and return message
        patientRepository.save(patient);

        return ResponseEntity.status(HttpStatus.CREATED).body("Patient " + patient.getFullName() + " added with id " + id);
    }

    @PostMapping("/doctor")
    public ResponseEntity<String> addNewDoctor(@RequestBody Doctor doctor) {
        String id = Utils.generateDoctorId(doctor.getFullName(),doctorRepository);
        doctor.setId(id);

        // Save doctor in repository and return message
        doctorRepository.save(doctor);

        return ResponseEntity.status(HttpStatus.CREATED).body("Doctor " + doctor.getFullName() + " added with id " + id);
    }

    @PostMapping("/specialty")
    public ResponseEntity<String> addNewSpecialty(@RequestBody String specialtyName) {
        String code = Utils.generateSpecialtyCode(specialtyName,specialtyRepository);
        Specialty specialty = new Specialty(code,specialtyName);

        // Save specialty in repository and return message
        specialtyRepository.save(specialty);

        return ResponseEntity.status(HttpStatus.CREATED).body("Specialty " + specialty.getName() + " added with code " + code);
    }

    @PostMapping("/medicine")
    public ResponseEntity<String> addNewMedicine(@RequestBody String medicineName) {
        Optional<Medicine> medicineOptional = medicineRepository.findByName(medicineName);
        if (medicineOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Medicine with name " + medicineOptional.get().getName() + " already exists (with id " + medicineOptional.get().getId() + ").");
        } else {
            Medicine medicine = new Medicine(medicineName);

            // Save medicine in repository and return message
            medicineRepository.save(medicine);

            return ResponseEntity.status(HttpStatus.CREATED).body("Medicine " + medicine.getName() + " added with id " + medicine.getId());
        }
    }

    @GetMapping("/patients")
    public ResponseEntity<?> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();
        if (patients.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No patients found.");
        }
        return ResponseEntity.ok(patients);
    }

    @GetMapping("/patient/{id}")
    public ResponseEntity<?> getPatientById(@PathVariable(name="id") String id) {
        Optional<Patient> patientOptional = patientRepository.findById(id);
        if (!patientOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient with id " + id + " not found.");
        }
        return ResponseEntity.ok(patientOptional.get());
    }

    @GetMapping("/doctors")
    public ResponseEntity<?> getAllDoctors() {
        List<Doctor> doctors = doctorRepository.findAll();
        if (doctors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No doctors found.");
        }
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/doctor/{id}")
    public ResponseEntity<?> getDoctorById(@PathVariable(name="id") String id) {
        Optional<Doctor> doctorOptional = doctorRepository.findById(id);
        if (!doctorOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor with id " + id + " not found.");
        }
        return ResponseEntity.ok(doctorOptional.get());
    }

    @GetMapping("/doctors/specialty/{code}")
    public ResponseEntity<?> getDoctorsBySpecialty(@PathVariable(name="code") String code) {
        Optional<Specialty> specialtyOptional = specialtyRepository.findByCode(code);
        if (!specialtyOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Specialty with code " + code + " not found.");
        }

        List<Doctor> doctors = doctorRepository.findBySpecialty(code);
        if (doctors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No doctors found with specialty " + specialtyOptional.get().getName() + ".");
        }

        return ResponseEntity.ok(doctors);
    }

    @GetMapping("appointments/{doctorId}")
    public ResponseEntity<?> getAppointmentsByDoctorId(@PathVariable(name="doctorId") String id) {
        Optional<Doctor> doctorOptional = doctorRepository.findById(id);
        if (!doctorOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Doctor with id " + id + " not found.");
        }

        List<Appointment> appointments = doctorOptional.get().getAppointments();
        if (appointments.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Any appointment found for doctor with id " + id);
        }

        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/specialties")
    public ResponseEntity<?> getAllSpecialties() {
        List<Specialty> specialties = specialtyRepository.findAll();
        if (specialties.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No specialties found.");
        }
        return ResponseEntity.ok(specialties);
    }

    @GetMapping("/specialty/{code}")
    public ResponseEntity<?> getSpecialtyByCode(@PathVariable(name="code") String code) {
        Optional<Specialty> specialtyOptional = specialtyRepository.findByCode(code);
        if (!specialtyOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Specialty with code " + code + " not found.");
        }
        return ResponseEntity.ok(specialtyOptional.get());
    }

    @GetMapping("/medicines")
    public ResponseEntity<?> getAllMedicines() {
        List<Medicine> medicines = medicineRepository.findAll();
        if (medicines.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No medicines found.");
        }
        return ResponseEntity.ok(medicines);
    }

    @GetMapping("/medicine/{id}")
    public ResponseEntity<?> getMedicineById(@PathVariable(name="id") int id) {
        Optional<Medicine> medicineOptional = medicineRepository.findById(id);
        if (!medicineOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Medicine with id " + id + " not found.");
        }
        return ResponseEntity.ok(medicineOptional.get());
    }

    @PostMapping("/appointment/{patientId}")
    public ResponseEntity<String> addNewAppointment(@RequestBody String dateString, @PathVariable(name="patientId") String patientId) throws Exception {
        Optional<Patient> patientOptional = patientRepository.findById(patientId);
        if (!patientOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient with id " + patientId + " not found.");
        } else {
            Date date = new Date();
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                date = formatter.parse(dateString);

                // Obtain current date
                Date currentDate = new Date();

                // Check if given date is posterior to current date
                if (date.before(currentDate) || date.equals(currentDate)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("The date of the appointment must be later than current date.");
                }

            } catch (ParseException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid date format. Please use dd/MM/yyyy.");
            }

            // Schedule appointment to the given patient and return message
            try {
                String out = appointmentsService.scheduleAppointment(patientOptional.get(), date);
                return ResponseEntity.status(HttpStatus.CREATED).body(out);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
        }
    }

}
