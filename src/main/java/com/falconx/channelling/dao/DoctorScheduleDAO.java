package com.falconx.channelling.dao;

import lombok.Data;

@Data
public class DoctorScheduleDAO {
    private long id;
    private String dayOfTheWeek;
    private String startTime;
    private String endTime;
    private boolean available;
}
