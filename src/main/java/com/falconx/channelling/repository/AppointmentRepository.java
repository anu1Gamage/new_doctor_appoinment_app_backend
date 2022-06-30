package com.falconx.channelling.repository;

import com.falconx.channelling.entities.Appointment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends CrudRepository<Appointment, Long> {

    boolean existsByDateAndTimeAndDoctor_Id(String date, String time, long doctorId);

    List<Appointment> getAllByDoctor_IdAndDate(long doctorId, String date);

    List<Appointment> getAllByPatient_Id(long patientId);

    Appointment getById(long id);

}
