package com.falconx.channelling.controller;

import com.falconx.channelling.dao.DoctorDAO;
import com.falconx.channelling.dto.DoctorDataPublicDTO;
import com.falconx.channelling.dto.UserDataPublicDTO;
import com.falconx.channelling.entities.Doctor;
import com.falconx.channelling.service.DoctorServiceImpl;
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
@RequestMapping("/doctor")
@CrossOrigin(origins = "*")
public class DoctorController {

    private final DoctorServiceImpl doctorService;

    public DoctorController(DoctorServiceImpl doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping("/{user-id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_DOCTOR') or hasRole('ROLE_RECEPTIONIST')")
    public ResponseEntity<DoctorDataPublicDTO> getDoctorByUserId(@PathVariable("user-id") String userId) {
        try {
            ResponseEntity<DoctorDataPublicDTO> doctorResponseEntity = doctorService.getDoctorByUserId(Long.parseLong(userId));
            return doctorResponseEntity;
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<DoctorDataPublicDTO>> getAllDoctors() {
        try {
            ResponseEntity<List<DoctorDataPublicDTO>> doctorResponseEntity = doctorService.getAllDoctors(true);
            return doctorResponseEntity;
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<DoctorDataPublicDTO> createDoctor(@RequestBody DoctorDAO doctorDAO) {
        try {
            ResponseEntity<DoctorDataPublicDTO> doctorResponseEntity = doctorService.createDoctor(doctorDAO);
            return doctorResponseEntity;
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping
    public ResponseEntity<List<DoctorDataPublicDTO>> getAllDoctorsByAvailability(@RequestParam(name = "availability") String availability) {
        try {
            ResponseEntity<List<DoctorDataPublicDTO>> doctorResponseEntity = doctorService.getAllDoctors(Boolean.parseBoolean(availability));
            return doctorResponseEntity;
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<DoctorDataPublicDTO>> searchDoctor(@RequestParam(name = "registration-number", required = false) String doctorRegistrationNumber,
                                                     @RequestParam(name = "specialization-id", required = false) String specializationId,
                                                     @RequestParam(name = "gender", required = false) String gender,
                                                     @RequestParam(name = "doctor-name", required = false) String doctorName) {
        try {

            ResponseEntity<List<DoctorDataPublicDTO>> doctorResponseEntity = null;

            if(doctorRegistrationNumber != null){
                doctorResponseEntity = doctorService
                        .searchDoctorsByDoctorRegistrationNumber(doctorRegistrationNumber);
            }
            if(specializationId != null && gender != null){
                doctorResponseEntity = doctorService
                        .searchDoctorsBySpecializationIdAndGender(Long.parseLong(specializationId), gender);
            }
            if(specializationId != null && gender == null){
                doctorResponseEntity = doctorService
                        .searchDoctorsBySpecializationId(Long.parseLong(specializationId));
            }
            if(doctorName != null && gender == null){
                doctorResponseEntity = doctorService
                        .searchDoctorsByName(doctorName);
            }
            if(doctorName != null && gender != null){
                doctorResponseEntity = doctorService
                        .searchDoctorsByNameAndGender(doctorName, gender);
            }
            if(doctorRegistrationNumber == null && specializationId == null && gender == null && doctorName == null){
                doctorResponseEntity = doctorService.getAllDoctors(true);
            }
            return doctorResponseEntity;
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PutMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_DOCTOR')")
    public ResponseEntity<Doctor> updateDoctor(@RequestBody DoctorDAO doctorDAO) {
        try {
            ResponseEntity<Doctor> doctorResponseEntity = doctorService.updateDoctor(doctorDAO);
            return doctorResponseEntity;
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @DeleteMapping("/{delete-id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Doctor> deleteDoctor(@PathVariable("delete-id") String doctorId) {
        try {
            ResponseEntity<Doctor> doctorResponseEntity = doctorService.deleteDoctor(Long.parseLong(doctorId));
            return doctorResponseEntity;
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

}
