package com.falconx.channelling.dto;

import lombok.Data;

import java.util.List;

@Data
public class DoctorScheduleDTO {
    private DoctorDataPublicDTO doctorData;
    private List<ScheduleDTO> schedules;
}
