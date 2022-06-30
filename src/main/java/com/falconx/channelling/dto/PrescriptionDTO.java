package com.falconx.channelling.dto;

import com.falconx.channelling.entities.Patient;
import com.falconx.channelling.entities.PrescriptionItem;
import lombok.Data;

import java.util.List;

@Data
public class PrescriptionDTO {
    private long id;
    private DoctorDataPublicDTO doctorData;
    private Patient patient;
    private String date;
    private String time;
    private List<PrescriptionItem> prescriptionItemList;
    private double totalCost;
    private boolean issuePharmacyItem;
}
