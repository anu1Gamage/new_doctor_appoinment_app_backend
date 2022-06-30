package com.falconx.channelling.service;

import com.falconx.channelling.entities.Patient;
import com.falconx.channelling.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientServiceImpl {

    private final PatientRepository patientRepository;

    @Autowired
    PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public ResponseEntity<Patient> registerPatient(Patient patient) {
        try {
            if (!patientRepository.existsByEmail(patient.getEmail())) {
                Patient registeredPatient = patientRepository.save(patient);
                return new ResponseEntity<>(registeredPatient, HttpStatus.OK);
            }
            return new ResponseEntity("patient already exist with the email", HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public ResponseEntity<List<Patient>> getAllPatients() {
        try {
            List<Patient> registeredPatient = (List<Patient>) patientRepository.findAll();
            if (!registeredPatient.isEmpty())
                return new ResponseEntity<>(registeredPatient, HttpStatus.OK);
            return new ResponseEntity("no patients found in the system", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public ResponseEntity<Patient> getPatientById(long patientId) {
        try {
            Optional<Patient> registeredPatient = patientRepository.findById(patientId);
            return registeredPatient.map(patient -> new ResponseEntity<>(patient, HttpStatus.OK)).orElseGet(() ->
                    new ResponseEntity("patient not found in the system", HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public ResponseEntity<Patient> getPatientByEmail(String email) {
        try {
            Optional<Patient> registeredPatient = patientRepository.findByEmail(email);
            return registeredPatient.map(patient -> new ResponseEntity<>(patient, HttpStatus.OK)).orElseGet(() ->
                    new ResponseEntity("patient not found in the system", HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public ResponseEntity<Patient> updatePatient(Patient patient) {
        try {
            Optional<Patient> registeredPatient = patientRepository.findById(patient.getId());
            if (registeredPatient.isPresent()) {

                registeredPatient.get().setAddress(patient.getAddress());
                registeredPatient.get().setContactNumber(patient.getContactNumber());
                registeredPatient.get().setDateOfBirth(patient.getDateOfBirth());
                registeredPatient.get().setEmail(patient.getEmail());
                registeredPatient.get().setEmergencyContactNumber(patient.getEmergencyContactNumber());
                registeredPatient.get().setFirstName(patient.getFirstName());
                registeredPatient.get().setGender(patient.getGender());
                registeredPatient.get().setLastName(patient.getLastName());
                Patient updatedPatient = patientRepository.save(patient);
                return new ResponseEntity<>(updatedPatient, HttpStatus.OK);
            }
            return new ResponseEntity("wrong patient id", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public ResponseEntity<Patient> deletePatientById(long patientId) {
        try {
            patientRepository.deleteById(patientId);
            return new ResponseEntity("deleted", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }


}
