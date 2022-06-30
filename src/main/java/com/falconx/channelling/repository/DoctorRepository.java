package com.falconx.channelling.repository;

import com.falconx.channelling.entities.Doctor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends CrudRepository<Doctor, Long> {
    Optional<Doctor> getDoctorByAppUser_Id(long userId);

    boolean existsById(long doctorId);

    List<Doctor> findAllByDoctorSpecialization_IdAndAvailable(long specialization, boolean isAvailable);

    List<Doctor> findAllByAvailable(boolean isAvailable);

    Optional<Doctor> getDoctorByDoctorRegistrationNumber(String doctorRegistrationNumber);

    List<Doctor> getDoctorByDoctorSpecialization_IdAndAndAppUser_Gender(long doctorSpecialization, String gender);

    List<Doctor> getDoctorByDoctorSpecialization_Id(long doctorSpecialization);

    @Query("Select d from Doctor d, AppUser a where a.id = d.appUser.id and lower(concat(d.appUser.firstName, d.appUser.lastName)) like %:userName%")
    List<Doctor> getDoctorByAppUserByFullName(String userName);

    @Query("Select d from Doctor d, AppUser a where a.id = d.appUser.id and lower(concat(d.appUser.firstName, d.appUser.lastName)) like %:userName% and d.appUser.gender = :gender")
    List<Doctor> getDoctorByAppUserByFullNameAndGender(String userName, String gender);

}
