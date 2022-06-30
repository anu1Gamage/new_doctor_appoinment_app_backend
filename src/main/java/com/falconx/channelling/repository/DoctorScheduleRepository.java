package com.falconx.channelling.repository;

import com.falconx.channelling.entities.DoctorSchedule;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DoctorScheduleRepository extends CrudRepository<DoctorSchedule, Long> {

    List<DoctorSchedule> findAllByDoctor_Id(long doctorId);
}
