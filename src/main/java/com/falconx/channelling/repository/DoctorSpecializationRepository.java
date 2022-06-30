package com.falconx.channelling.repository;

import com.falconx.channelling.entities.DoctorSpecialization;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorSpecializationRepository extends CrudRepository<DoctorSpecialization, Long> {

    List<DoctorSpecialization> findAllByAvailable(boolean available);

}
