package com.falconx.channelling.dto;

import lombok.Data;

@Data
public class ScheduleDTO {
    private long id;
    private String dayOfTheWeek;
    private String startTime;
    private String endTime;
    private boolean available;
}
