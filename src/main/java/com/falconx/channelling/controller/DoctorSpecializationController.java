package com.falconx.channelling.controller;

import com.falconx.channelling.dao.DoctorSpecializationDAO;
import com.falconx.channelling.entities.DoctorSpecialization;
import com.falconx.channelling.service.DoctorSpecializationServiceImpl;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/doctor-specialization")
@CrossOrigin(origins = "*")
public class DoctorSpecializationController {

    DoctorSpecializationServiceImpl specializationService;
    ModelMapper modelMapper;

    public DoctorSpecializationController(DoctorSpecializationServiceImpl specializationService, ModelMapper modelMapper) {
        this.specializationService = specializationService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<DoctorSpecialization> createDoctorSpecialization(@RequestBody DoctorSpecializationDAO specializationDAO) {
        try {
            ResponseEntity<DoctorSpecialization> doctorSpecializationEntity = specializationService.addDoctorSpecialization(modelMapper.map(specializationDAO, DoctorSpecialization.class));
            return doctorSpecializationEntity;
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<DoctorSpecialization>> getAllDoctorSpecialization() {
        try {
            ResponseEntity<List<DoctorSpecialization>> appointmentResponseEntity = specializationService.getAllDoctorSpecialization();
            return appointmentResponseEntity;
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PutMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<DoctorSpecialization> updateDoctorSpecialization(@RequestBody DoctorSpecializationDAO specializationDAO) {
        try {
            ResponseEntity<DoctorSpecialization> appointmentResponseEntity = specializationService.updateDoctorSpecialization(modelMapper.map(specializationDAO, DoctorSpecialization.class));
            return appointmentResponseEntity;
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @DeleteMapping("/{specialization-id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<DoctorSpecialization> deleteDoctorSpecialization(@PathVariable("specialization-id") String specializationId) {
        try {
            ResponseEntity<DoctorSpecialization> appointmentResponseEntity = specializationService.deleteDoctorSpecialization(Long.parseLong(specializationId));
            return appointmentResponseEntity;
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

}
