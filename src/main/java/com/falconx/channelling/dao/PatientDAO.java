package com.falconx.channelling.dao;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PatientDAO {
    private String firstName;
    private String lastName;
    private String gender;
    private String address;
    private String email;
    private String contactNumber;
    private String emergencyContactNumber;
    private String dateOfBirth;
}
