package com.falconx.channelling.service;

import com.falconx.channelling.dao.AppointmentDAO;
import com.falconx.channelling.dto.AppointmentDTO;
import com.falconx.channelling.entities.Appointment;
import com.falconx.channelling.entities.Doctor;
import com.falconx.channelling.entities.Patient;
import com.falconx.channelling.repository.AppointmentRepository;
import com.falconx.channelling.repository.DoctorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentServiceImpl {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientServiceImpl patientService;
    private final DoctorServiceImpl doctorService;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, DoctorRepository doctorRepository,
                                  PatientServiceImpl patientService, DoctorServiceImpl doctorService) {
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
        this.patientService = patientService;
        this.doctorService = doctorService;
    }

    public ResponseEntity<AppointmentDTO> createAppointment(AppointmentDAO appointmentDAO) {
        try {

            if (!appointmentRepository.existsByDateAndTimeAndDoctor_Id(appointmentDAO.getDate(),
                    appointmentDAO.getTime(),
                    appointmentDAO.getDoctorId()) && doctorRepository.existsById(appointmentDAO.getDoctorId())) {

                Appointment appointment = new Appointment();
                appointment.setTerminated(false);
                appointment.setDate(appointmentDAO.getDate());
                Optional<Doctor> doctor = doctorRepository.findById(appointmentDAO.getDoctorId());
                appointment.setDoctor(doctor.get());

                ResponseEntity<Patient> patient = patientService.getPatientById(appointmentDAO.getPatientId());
                appointment.setPatient(patient.getBody());
                appointment.setTime(appointmentDAO.getTime());
                appointment.setDescription(appointmentDAO.getDescription());

                Appointment createdAppointment = appointmentRepository.save(appointment);
                AppointmentDTO appointmentDTO = mapAppointment(createdAppointment);
                return new ResponseEntity<>(appointmentDTO, HttpStatus.OK);
            }
            return new ResponseEntity("appointment already exists for the selected time slot", HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByDoctor(long doctorId, String date) {
        List<AppointmentDTO> appointmentDTOList = new ArrayList<>();
        try {
            List<Appointment> appointmentList = appointmentRepository.getAllByDoctor_IdAndDate(doctorId, date);
            if (!appointmentList.isEmpty()) {

                appointmentList.forEach(appointment -> {
                    appointmentDTOList.add(mapAppointment(appointment));
                });

                return new ResponseEntity<>(appointmentDTOList, HttpStatus.OK);
            }
            return new ResponseEntity("no appointments exists", HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public ResponseEntity<List<AppointmentDTO>> getAppointmentsByPatient(long patientId) {
        List<AppointmentDTO> appointmentDTOList = new ArrayList<>();
        try {
            List<Appointment> appointmentList = appointmentRepository.getAllByPatient_Id(patientId);
            if (!appointmentList.isEmpty()) {
                appointmentList.forEach(appointment -> {
                    appointmentDTOList.add(mapAppointment(appointment));
                });

                return new ResponseEntity<>(appointmentDTOList, HttpStatus.OK);
            }
            return new ResponseEntity("no appointments exists", HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public ResponseEntity<AppointmentDTO> terminateAppointment(long appointmentId) {
        try {
            Appointment appointment = appointmentRepository.getById(appointmentId);
            appointment.setTerminated(true);
            Appointment updated = appointmentRepository.save(appointment);
            if (updated != null) {
                AppointmentDTO appointmentDTO = mapAppointment(updated);
                return new ResponseEntity<>(appointmentDTO, HttpStatus.OK);
            }
            return new ResponseEntity("no appointments exists", HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public AppointmentDTO mapAppointment(Appointment appointment){
        AppointmentDTO appointmentDTO = new AppointmentDTO();

        appointmentDTO.setId(appointment.getId());
        appointmentDTO.setDate(appointment.getDate());
        appointmentDTO.setTime(appointment.getTime());
        appointmentDTO.setTerminated(appointment.isTerminated());
        appointmentDTO.setDescription(appointment.getDescription());
        appointmentDTO.setDoctorData(doctorService.mapDoctorData(appointment.getDoctor()));
        appointmentDTO.setPatient(appointment.getPatient());

        return appointmentDTO;

    }

}
