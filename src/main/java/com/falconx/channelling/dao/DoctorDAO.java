package com.falconx.channelling.dao;

import com.falconx.channelling.entities.AppUserRole;
import lombok.Data;

import java.util.List;

@Data
public class DoctorDAO {
    private long id;
    private long appUserId;
    private String username;
    private String firstName;
    private String lastName;
    private String contactNumber;
    private String gender;
    private String password;
    List<AppUserRole> appUserRoles;
    private String doctorRegistrationNumber;
    private long doctorSpecializationId;
    private double rate;
    private boolean available;
}
