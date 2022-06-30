package com.falconx.channelling.dto;

import com.falconx.channelling.entities.AppUserRole;
import lombok.Data;

import java.util.List;

@Data
public class UserResponseDTO {

    private Integer id;
    private String firstName;
    private String lastName;
    private String username;
    List<AppUserRole> appUserRoles;

}
