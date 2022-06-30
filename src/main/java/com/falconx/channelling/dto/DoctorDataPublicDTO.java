package com.falconx.channelling.dto;

import com.falconx.channelling.entities.DoctorSpecialization;
import lombok.Data;

@Data
public class DoctorDataPublicDTO {
    private long id;
    private String firstName;
    private String lastName;
    private String contactNumber;
    private String gender;
    private String doctorRegistrationNumber;
    private DoctorSpecialization doctorSpecialization;
    private double rate;
    private boolean available;
}
