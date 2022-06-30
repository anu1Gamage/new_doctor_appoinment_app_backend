package com.falconx.channelling.dao;

import lombok.Data;

@Data
public class DoctorSpecializationDAO {
    private long id;
    private String name;
    private boolean available;
}
