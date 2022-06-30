package com.falconx.channelling.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@Entity
@Table(name = "doctor")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne(cascade= CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private AppUser appUser;
    private String doctorRegistrationNumber;
    @OneToOne(cascade= CascadeType.MERGE)
    private DoctorSpecialization doctorSpecialization;
    private double rate;
    private boolean available;
    private boolean status;
}
