package com.falconx.channelling.service;

import com.falconx.channelling.entities.DoctorSpecialization;
import com.falconx.channelling.repository.DoctorSpecializationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorSpecializationServiceImpl {

    private DoctorSpecializationRepository doctorSpecializationRepository;

    DoctorSpecializationServiceImpl(DoctorSpecializationRepository doctorSpecializationRepository) {
        this.doctorSpecializationRepository = doctorSpecializationRepository;
    }

    public ResponseEntity<DoctorSpecialization> addDoctorSpecialization(DoctorSpecialization doctorSpecialization) {
        try {
            DoctorSpecialization createdDoctorSpecialization = doctorSpecializationRepository.save(doctorSpecialization);
            return new ResponseEntity<>(createdDoctorSpecialization, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public ResponseEntity<List<DoctorSpecialization>> getAllDoctorSpecialization() {
        try {
            List<DoctorSpecialization> createdDoctorSpecialization = doctorSpecializationRepository.findAllByAvailable(true);
            return new ResponseEntity<>(createdDoctorSpecialization, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public ResponseEntity<DoctorSpecialization> getDoctorSpecializationById(long id) {
        try {
            Optional<DoctorSpecialization> specialization = doctorSpecializationRepository.findById(id);
            if(specialization.isPresent())
                return new ResponseEntity<>(specialization.get(), HttpStatus.OK);
            return new ResponseEntity("doctor specialization not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }


    public ResponseEntity<DoctorSpecialization> updateDoctorSpecialization(DoctorSpecialization doctorSpecialization) {
        try {
            Optional<DoctorSpecialization> specialization = doctorSpecializationRepository.findById(doctorSpecialization.getId());
            if(specialization.isPresent()) {
                specialization.get().setName(doctorSpecialization.getName());
                specialization.get().setAvailable(doctorSpecialization.isAvailable());
                DoctorSpecialization createdDoctorSpecialization = doctorSpecializationRepository.save(specialization.get());

                return new ResponseEntity<>(createdDoctorSpecialization, HttpStatus.OK);
            }
            return new ResponseEntity("no specialization found", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public ResponseEntity<DoctorSpecialization> deleteDoctorSpecialization(long specializationId) {
        try {
            Optional<DoctorSpecialization> specialization = doctorSpecializationRepository.findById(specializationId);
            if(specialization.isPresent()) {
                specialization.get().setAvailable(false);
                doctorSpecializationRepository.save(specialization.get());
                return new ResponseEntity("deleted", HttpStatus.OK);
            }
            return new ResponseEntity("no specialization found", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

}
