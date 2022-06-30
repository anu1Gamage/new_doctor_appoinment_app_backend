package com.falconx.channelling.repository;

import com.falconx.channelling.entities.Prescription;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepository extends CrudRepository<Prescription, Long> {
    List<Prescription> findAllByIssuePharmacyItem(boolean issued);
}
