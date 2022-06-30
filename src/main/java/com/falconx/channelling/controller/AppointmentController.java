package com.falconx.channelling.controller;

import com.falconx.channelling.dao.AppointmentDAO;
import com.falconx.channelling.dto.AppointmentDTO;
import com.falconx.channelling.entities.Appointment;
import com.falconx.channelling.service.AppointmentServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/appointment")
@CrossOrigin(origins = "*")
public class AppointmentController {

    private AppointmentServiceImpl appointmentService;

    AppointmentController(AppointmentServiceImpl appointmentService) {
        this.appointmentService = appointmentService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_RECEPTIONIST')")
    public ResponseEntity<AppointmentDTO> createAppointment(@RequestBody AppointmentDAO appointmentDAO) {
        try {
            ResponseEntity<AppointmentDTO> appointmentResponseEntity = appointmentService.createAppointment(appointmentDAO);
            return appointmentResponseEntity;
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/get-by-doctor")
    @PreAuthorize("hasRole('ROLE_RECEPTIONIST') or hasRole('ROLE_DOCTOR')")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByDoctor(@RequestParam(name = "doctor-id") String doctorId, @RequestParam(name = "date") String date) {
        try {
            ResponseEntity<List<AppointmentDTO>> medicineResponseEntity = appointmentService.getAppointmentsByDoctor(Long.parseLong(doctorId), date);
            return medicineResponseEntity;
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/get-by-patient")
    @PreAuthorize("hasRole('ROLE_RECEPTIONIST') or hasRole('ROLE_DOCTOR')")
    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByPatient(@RequestParam(name = "patient-id") String patientId) {
        try {
            ResponseEntity<List<AppointmentDTO>> medicineResponseEntity = appointmentService.getAppointmentsByPatient(Long.parseLong(patientId));
            return medicineResponseEntity;
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @PutMapping("/terminate")
    @PreAuthorize("hasRole('ROLE_RECEPTIONIST') or hasRole('ROLE_DOCTOR')")
    public ResponseEntity<AppointmentDTO> terminateAppointment(@RequestParam(name = "appointment-id") String appointmentId) {
        try {
            ResponseEntity<AppointmentDTO> medicineResponseEntity = appointmentService.terminateAppointment(Long.parseLong(appointmentId));
            return medicineResponseEntity;
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
