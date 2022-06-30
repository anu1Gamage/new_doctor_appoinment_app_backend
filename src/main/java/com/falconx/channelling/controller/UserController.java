package com.falconx.channelling.controller;

import com.falconx.channelling.dao.LoginUserDAO;
import com.falconx.channelling.dto.UserDataDTO;
import com.falconx.channelling.dto.UserResponseDTO;
import com.falconx.channelling.entities.AppUser;
import com.falconx.channelling.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/signin")
    public String login(@RequestBody LoginUserDAO loginUserDAO) {
        return userService.signin(loginUserDAO.getUsername(), loginUserDAO.getPassword());
    }

    @PostMapping("/signup")
    public String signup(@RequestBody UserDataDTO user) {
        return userService.signup(modelMapper.map(user, AppUser.class));
    }

    @DeleteMapping(value = "/{user-id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String delete(@PathVariable("user-id") String userId) {
        userService.delete(Long.parseLong(userId));
        return userId;
    }

    @GetMapping(value = "/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public UserResponseDTO search(@PathVariable String username) {
        return modelMapper.map(userService.search(username), UserResponseDTO.class);
    }

    @GetMapping(value = "/user-details")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_RECEPTIONIST') or hasRole('ROLE_PHARMACIST') or hasRole('ROLE_DOCTOR')")
    public UserResponseDTO userDetails(HttpServletRequest req) {
        return modelMapper.map(userService.userDetails(req), UserResponseDTO.class);
    }
}
