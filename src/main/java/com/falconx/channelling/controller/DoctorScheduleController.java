package com.falconx.channelling.controller;

import com.falconx.channelling.dao.DoctorScheduleDAO;
import com.falconx.channelling.dto.DoctorScheduleDTO;
import com.falconx.channelling.entities.Doctor;
import com.falconx.channelling.entities.DoctorSchedule;
import com.falconx.channelling.repository.DoctorScheduleRepository;
import com.falconx.channelling.service.DoctorScheduleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/schedule")
@CrossOrigin(origins = "*")
public class DoctorScheduleController {

    DoctorScheduleServiceImpl doctorScheduleService;

    @Autowired
    DoctorScheduleController(DoctorScheduleServiceImpl doctorScheduleService){
        this.doctorScheduleService = doctorScheduleService;
    }

    @GetMapping("/{doctor-id}")
    @PreAuthorize("hasRole('ROLE_DOCTOR') or  hasRole('ROLE_RECEPTIONIST')")
    public ResponseEntity<DoctorScheduleDTO> getSchedulesByDoctorId(@PathVariable("doctor-id") String doctorId) {
        try {
            ResponseEntity<DoctorScheduleDTO> medicineResponseEntity = doctorScheduleService.getSchedulesByDoctorId(Long.parseLong(doctorId));
            return medicineResponseEntity;
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PostMapping("/{doctor-id}")
    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    public ResponseEntity<DoctorScheduleDTO> createSchedulesByDoctor(@RequestBody List<DoctorScheduleDAO> doctorScheduleDAO, @PathVariable("doctor-id") String doctorId) {
        try {
            ResponseEntity<DoctorScheduleDTO> medicineResponseEntity = doctorScheduleService.createSchedulesByDoctor(doctorScheduleDAO, Long.parseLong(doctorId));
            return medicineResponseEntity;
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PutMapping("/{doctor-id}")
    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    public ResponseEntity<DoctorScheduleDTO> updateSchedulesByDoctorId(@RequestBody List<DoctorScheduleDAO> doctorScheduleDAO, @PathVariable("doctor-id") String doctorId) {
        try {
            ResponseEntity<DoctorScheduleDTO> medicineResponseEntity = doctorScheduleService.updateSchedulesByDoctorId(doctorScheduleDAO, Long.parseLong(doctorId));
            return medicineResponseEntity;
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

}
