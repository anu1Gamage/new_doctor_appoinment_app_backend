package com.falconx.channelling.service;

import com.falconx.channelling.dao.DoctorScheduleDAO;
import com.falconx.channelling.dto.DoctorScheduleDTO;
import com.falconx.channelling.dto.ScheduleDTO;
import com.falconx.channelling.entities.Doctor;
import com.falconx.channelling.entities.DoctorSchedule;
import com.falconx.channelling.repository.DoctorRepository;
import com.falconx.channelling.repository.DoctorScheduleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorScheduleServiceImpl {

    private DoctorScheduleRepository doctorScheduleRepository;
    private DoctorRepository doctorRepository;
    private DoctorServiceImpl doctorService;

    DoctorScheduleServiceImpl(DoctorScheduleRepository doctorScheduleRepository, DoctorRepository doctorRepository,
                              DoctorServiceImpl doctorService) {
        this.doctorScheduleRepository = doctorScheduleRepository;
        this.doctorRepository = doctorRepository;
        this.doctorService = doctorService;
    }

    public ResponseEntity<DoctorScheduleDTO> getSchedulesByDoctorId(long doctorId) {
        List<ScheduleDTO> doctorScheduleDTOList = new ArrayList<>();
        DoctorScheduleDTO doctorScheduleDTO = new DoctorScheduleDTO();

        try {
            List<DoctorSchedule> scheduleList = doctorScheduleRepository.findAllByDoctor_Id(doctorId);
            if (!scheduleList.isEmpty()) {
                scheduleList.forEach(doctorSchedule -> {
                    doctorScheduleDTO.setDoctorData(doctorService.mapDoctorData(doctorSchedule.getDoctor()));
                    doctorScheduleDTOList.add(mapSchedule(doctorSchedule));
                });
                doctorScheduleDTO.setSchedules(doctorScheduleDTOList);
                return new ResponseEntity<>(doctorScheduleDTO, HttpStatus.OK);
            }
            return new ResponseEntity("no schedules exists", HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public ResponseEntity<DoctorScheduleDTO> createSchedulesByDoctor(List<DoctorScheduleDAO> doctorScheduleDAOList, long doctorId) {

        List<ScheduleDTO> doctorScheduleDTOList = new ArrayList<>();
        DoctorScheduleDTO doctorScheduleDTO = new DoctorScheduleDTO();

        try {
            Optional<Doctor> doctor = doctorRepository.findById(doctorId);

            if (doctor.isPresent()) {

                doctorScheduleDTO.setDoctorData(doctorService.mapDoctorData(doctor.get()));

                for (DoctorScheduleDAO schedule : doctorScheduleDAOList) {
                    DoctorSchedule doctorSchedule = new DoctorSchedule();
                    doctorSchedule.setAvailable(schedule.isAvailable());
                    doctorSchedule.setDayOfTheWeek(schedule.getDayOfTheWeek());
                    doctorSchedule.setDoctor(doctor.get());
                    doctorSchedule.setEndTime(schedule.getEndTime());
                    doctorSchedule.setStartTime(schedule.getStartTime());
                    DoctorSchedule createdSchedule = doctorScheduleRepository.save(doctorSchedule);
                    doctorScheduleDTOList.add(mapSchedule(createdSchedule));
                }

                doctorScheduleDTO.setSchedules(doctorScheduleDTOList);

                return new ResponseEntity<>(doctorScheduleDTO, HttpStatus.OK);

            }
            return new ResponseEntity("no doctor exists", HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public ResponseEntity<DoctorScheduleDTO> updateSchedulesByDoctorId(List<DoctorScheduleDAO> doctorScheduleDAOList, long doctorId) {

        List<ScheduleDTO> doctorScheduleDTOList = new ArrayList<>();
        DoctorScheduleDTO doctorScheduleDTO = new DoctorScheduleDTO();

        try {
            Optional<Doctor> doctor = doctorRepository.findById(doctorId);

            List<DoctorSchedule> allSchedulesByDoctorId = doctorScheduleRepository.findAllByDoctor_Id(doctorId);

            if (doctor.isPresent()) {

                doctorScheduleDTO.setDoctorData(doctorService.mapDoctorData(doctor.get()));

                for (DoctorSchedule doctorSchedule : allSchedulesByDoctorId) {
                    doctorScheduleDAOList.forEach(doctorScheduleDAO -> {
                        if(doctorSchedule.getId() == doctorScheduleDAO.getId()){
                            doctorSchedule.setAvailable(doctorScheduleDAO.isAvailable());
                            doctorSchedule.setEndTime(doctorScheduleDAO.getEndTime());
                            doctorSchedule.setStartTime(doctorScheduleDAO.getStartTime());

                            DoctorSchedule createdSchedule = doctorScheduleRepository.save(doctorSchedule);
                            doctorScheduleDTOList.add(mapSchedule(createdSchedule));
                        }
                    });
                }
                doctorScheduleDTO.setSchedules(doctorScheduleDTOList);
                return new ResponseEntity<>(doctorScheduleDTO, HttpStatus.OK);
            }
            return new ResponseEntity("no doctor exists", HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    private ScheduleDTO mapSchedule(DoctorSchedule doctorSchedule){
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setId(doctorSchedule.getId());
        scheduleDTO.setDayOfTheWeek(doctorSchedule.getDayOfTheWeek());
        scheduleDTO.setStartTime(doctorSchedule.getStartTime());
        scheduleDTO.setEndTime(doctorSchedule.getEndTime());
        scheduleDTO.setAvailable(doctorSchedule.isAvailable());

        return scheduleDTO;

    }

}
