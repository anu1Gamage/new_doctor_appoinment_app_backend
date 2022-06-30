package com.falconx.channelling.dto;

import com.falconx.channelling.entities.Patient;
import lombok.Data;

@Data
public class AppointmentDTO {
    private long id;
    private String date;
    private String time;
    private DoctorDataPublicDTO doctorData;
    private Patient patient;
    private String description;
    private boolean terminated;
}
