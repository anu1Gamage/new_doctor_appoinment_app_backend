package com.falconx.channelling.dao;

import lombok.Data;

import java.util.List;

@Data
public class PrescriptionDAO {
    private long id;
    private String doctorId;
    private String patientId;
    private List<MedicineDAO> medicineList;
    private boolean issuePharmacyItem;
}
