package com.falconx.channelling.entities;

import lombok.Data;

import java.util.List;

@Data
public class Bill {
    private long id;
    private Appointment appointment;
    private List<PharmacyItem> pharmacyItemList;
    private double pharmacyItemCost;
    private double channellingConst;
    private double totalCost;
    private String date;
    private String time;
}
