package com.falconx.channelling.dao;

import lombok.Data;

@Data
public class AppointmentDAO {
    private String date;
    private String time;
    private long doctorId;
    private long patientId;
    private String description;
}
