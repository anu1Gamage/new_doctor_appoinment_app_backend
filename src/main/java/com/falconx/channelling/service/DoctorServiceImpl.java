package com.falconx.channelling.service;

import com.falconx.channelling.dao.DoctorDAO;
import com.falconx.channelling.dto.DoctorDataPublicDTO;
import com.falconx.channelling.dto.UserDataPublicDTO;
import com.falconx.channelling.entities.AppUser;
import com.falconx.channelling.entities.Doctor;
import com.falconx.channelling.entities.DoctorSpecialization;
import com.falconx.channelling.repository.DoctorRepository;
import com.falconx.channelling.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorServiceImpl {

    private final DoctorRepository doctorRepository;
    private final UserService userService;
    private final DoctorSpecializationServiceImpl doctorSpecialization;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public DoctorServiceImpl(DoctorRepository doctorRepository, UserService userService, UserRepository userRepository,
                             DoctorSpecializationServiceImpl doctorSpecialization, ModelMapper modelMapper) {
        this.doctorRepository = doctorRepository;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.doctorSpecialization = doctorSpecialization;
        this.userRepository = userRepository;
    }

    public ResponseEntity<DoctorDataPublicDTO> getDoctorByUserId(long userId) {

        try {
            Optional<Doctor> doctor = doctorRepository.getDoctorByAppUser_Id(userId);

            if(doctor.isPresent()){
                DoctorDataPublicDTO doctorDataPublicDTO = mapDoctorData(doctor.get());
                return new ResponseEntity<>(doctorDataPublicDTO, HttpStatus.OK);
            }

            return new ResponseEntity("no doctor found with the user id", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public ResponseEntity<List<DoctorDataPublicDTO>> getAllDoctors(boolean isAvailable) {
        List<DoctorDataPublicDTO> doctorDataList = new ArrayList<>();

        try {
            List<Doctor> doctorList = doctorRepository.findAllByAvailable(isAvailable);
            doctorList.forEach(doctor -> {
                doctorDataList.add(mapDoctorData(doctor));
            });
            return new ResponseEntity<>(doctorDataList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public ResponseEntity<DoctorDataPublicDTO> createDoctor(DoctorDAO doctorDAO) {
        try {

            Doctor doctor = new Doctor();
            doctor.setAvailable(doctorDAO.isAvailable());
            ResponseEntity<DoctorSpecialization> doctorSpecializationById = doctorSpecialization.getDoctorSpecializationById(doctorDAO.getDoctorSpecializationId());
            doctor.setDoctorRegistrationNumber(doctorDAO.getDoctorRegistrationNumber());
            doctor.setDoctorSpecialization(doctorSpecializationById.getBody());
            doctor.setRate(doctorDAO.getRate());

            AppUser appUser = new AppUser();
            appUser.setFirstName(doctorDAO.getFirstName());
            appUser.setLastName(doctorDAO.getLastName());
            appUser.setPassword(doctorDAO.getPassword());
            appUser.setUsername(doctorDAO.getUsername());
            appUser.setAppUserRoles(doctorDAO.getAppUserRoles());
            appUser.setGender(doctorDAO.getGender());
            appUser.setContactNumber(doctorDAO.getContactNumber());

            String token = userService.signup(appUser);
            if (token != null) {
                AppUser userByToken = userService.getUserByToken(token);
                doctor.setAppUser(userByToken);
                Doctor registeredDoctor = doctorRepository.save(doctor);
                DoctorDataPublicDTO doctorData = mapDoctorData(registeredDoctor);
                return new ResponseEntity<>(doctorData, HttpStatus.OK);
            }
            return new ResponseEntity("user creation failed", HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public ResponseEntity<Doctor> updateDoctor(DoctorDAO doctorDAO) {
        try {
            Optional<Doctor> doctor = doctorRepository.findById(doctorDAO.getId());
            if (doctor.isPresent()) {
                doctor.get().setAvailable(doctorDAO.isAvailable());
                doctor.get().setDoctorRegistrationNumber(doctorDAO.getDoctorRegistrationNumber());

                Optional<AppUser> userById = userRepository.findById(doctorDAO.getAppUserId());

                userById.get().setFirstName(doctorDAO.getFirstName());
                userById.get().setLastName(doctorDAO.getLastName());
                userById.get().setUsername(doctorDAO.getUsername());
                userById.get().setAppUserRoles(doctorDAO.getAppUserRoles());
                userById.get().setGender(doctorDAO.getGender());
                userById.get().setContactNumber(doctorDAO.getContactNumber());

                userRepository.save(userById.get());

                Doctor registeredDoctor = doctorRepository.save(doctor.get());
                return new ResponseEntity<>(registeredDoctor, HttpStatus.OK);
            }
            return new ResponseEntity("no doctors found with provided data", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public ResponseEntity<Doctor> deleteDoctor(long doctorId) {
        try {
            Optional<Doctor> doctor = doctorRepository.findById(doctorId);
            if (doctor.isPresent()) {
                doctorRepository.deleteById(doctorId);
                return new ResponseEntity("deleted", HttpStatus.OK);
            }
            return new ResponseEntity("no doctors found with provided data", HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public ResponseEntity<List<DoctorDataPublicDTO>> searchDoctorsByDoctorRegistrationNumber(String doctorRegistrationNumber) {
        List<DoctorDataPublicDTO> doctorList = new ArrayList<>();
        try {
            Optional<Doctor> doctor = doctorRepository.getDoctorByDoctorRegistrationNumber(doctorRegistrationNumber);
            if(doctor.isPresent()){
                doctorList.add(mapDoctorData(doctor.get()));
                return new ResponseEntity<>(doctorList, HttpStatus.OK);
            }
            return new ResponseEntity("no doctors found with provided data", HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public ResponseEntity<List<DoctorDataPublicDTO>> searchDoctorsBySpecializationIdAndGender(long specializationId, String gender) {
        List<DoctorDataPublicDTO> doctorList = new ArrayList<>();
        try {
            List<Doctor> doctorData = doctorRepository.getDoctorByDoctorSpecialization_IdAndAndAppUser_Gender(specializationId, gender);
            if(!doctorData.isEmpty()){
                doctorData.forEach(doctor -> {
                    doctorList.add(mapDoctorData(doctor));
                });
                return new ResponseEntity<>(doctorList, HttpStatus.OK);
            }
            return new ResponseEntity("no doctors found with provided data", HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public ResponseEntity<List<DoctorDataPublicDTO>> searchDoctorsBySpecializationId(long specializationId) {
        List<DoctorDataPublicDTO> doctorList = new ArrayList<>();
        try {
            List<Doctor> doctorData = doctorRepository.getDoctorByDoctorSpecialization_Id(specializationId);
            if(!doctorData.isEmpty()){
                doctorData.forEach(doctor -> {
                    doctorList.add(mapDoctorData(doctor));
                });
                return new ResponseEntity<>(doctorList, HttpStatus.OK);
            }
            return new ResponseEntity("no doctors found with provided data", HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public ResponseEntity<List<DoctorDataPublicDTO>> searchDoctorsByName(String doctorName) {
        List<DoctorDataPublicDTO> doctorList = new ArrayList<>();
        try {
            List<Doctor> doctorData = doctorRepository.getDoctorByAppUserByFullName(doctorName.toLowerCase());
            if(!doctorData.isEmpty()){
                doctorData.forEach(doctor -> {
                    doctorList.add(mapDoctorData(doctor));
                });
                return new ResponseEntity<>(doctorList, HttpStatus.OK);
            }
            return new ResponseEntity("no doctors found with provided data", HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public ResponseEntity<List<DoctorDataPublicDTO>> searchDoctorsByNameAndGender(String doctorName, String gender) {
        List<DoctorDataPublicDTO> doctorList = new ArrayList<>();
        try {
            List<Doctor> doctorData = doctorRepository.getDoctorByAppUserByFullNameAndGender(doctorName.toLowerCase(), gender);
            if(!doctorData.isEmpty()){
                doctorData.forEach(doctor -> {
                    doctorList.add(mapDoctorData(doctor));
                });
                return new ResponseEntity<>(doctorList, HttpStatus.OK);
            }
            return new ResponseEntity("no doctors found with provided data", HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (Exception e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public DoctorDataPublicDTO mapDoctorData(Doctor doctor){
        DoctorDataPublicDTO map = modelMapper.map(doctor.getAppUser(), DoctorDataPublicDTO.class);
        map.setId(doctor.getId());
        map.setDoctorSpecialization(doctor.getDoctorSpecialization());
        map.setDoctorRegistrationNumber(doctor.getDoctorRegistrationNumber());
        map.setRate(doctor.getRate());
        map.setAvailable(doctor.isAvailable());
        return map;
    }


}
