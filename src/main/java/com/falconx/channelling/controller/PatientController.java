package com.falconx.channelling.controller;

import com.falconx.channelling.dao.PatientDAO;
import com.falconx.channelling.entities.Patient;
import com.falconx.channelling.service.PatientServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/patient")
@CrossOrigin(origins = "*")
public class PatientController {

    private final ModelMapper modelMapper;
    private final PatientServiceImpl patientService;

    public PatientController(ModelMapper modelMapper, PatientServiceImpl patientService) {
        this.modelMapper = modelMapper;
        this.patientService = patientService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_RECEPTIONIST') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Patient> registerPatient(@RequestBody PatientDAO patientDAO) {
        try {
            ResponseEntity<Patient> patient = patientService.registerPatient(modelMapper.map(patientDAO, Patient.class));
            return patient;
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_RECEPTIONIST') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Patient>> getAllPatients() {
        try {
            ResponseEntity<List<Patient>> patient = patientService.getAllPatients();
            return patient;
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/{patient-id}")
    @PreAuthorize("hasRole('ROLE_RECEPTIONIST') or hasRole('ROLE_DOCTOR') or hasRole('ROLE_PHARMACIST') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Patient> getPatientDetails(@PathVariable("patient-id") String patientId) {
        try {
            ResponseEntity<Patient> patient = patientService.getPatientById(Long.parseLong(patientId));
            return patient;
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PutMapping
    @PreAuthorize("hasRole('ROLE_RECEPTIONIST') or hasRole('ROLE_DOCTOR') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<Patient> updatePatient(@RequestBody Patient patientDao) {
        try {
            ResponseEntity<Patient> patient = patientService.updatePatient(patientDao);
            return patient;
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("search")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_DOCTOR') or hasRole('ROLE_PHARMACIST') or hasRole('ROLE_RECEPTIONIST')")
    public ResponseEntity<Patient> getPatientByEmail(@RequestParam("email") String patientEmail) {
        try {
            ResponseEntity<Patient> patient = patientService.getPatientByEmail(patientEmail);
            return patient;
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @DeleteMapping("/{patient-id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECEPTIONIST')")
    public ResponseEntity<Patient> deletePatientByEmail(@PathVariable("patient-id") String patientId) {
        try {
            ResponseEntity<Patient> patient = patientService.deletePatientById(Long.parseLong(patientId));
            return patient;
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

}
