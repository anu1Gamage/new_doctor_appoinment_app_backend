package com.falconx.channelling.repository;

import com.falconx.channelling.entities.Patient;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends CrudRepository<Patient, Long> {

    boolean existsByEmail(String email);

    boolean existsById(long id);

    Optional<Patient> findByEmail(String email);

    @Override
    Iterable<Patient> findAll();

    Optional<Patient> findById(long patientId);
}
