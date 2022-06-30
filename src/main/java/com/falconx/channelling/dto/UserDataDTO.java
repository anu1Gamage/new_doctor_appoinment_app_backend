package com.falconx.channelling.dto;

import com.falconx.channelling.entities.AppUserRole;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UserDataDTO {
    private String username;
    private String firstName;
    private String lastName;
    private String contactNumber;
    private String gender;
    private String email;
    private String password;
    List<AppUserRole> appUserRoles;
}
