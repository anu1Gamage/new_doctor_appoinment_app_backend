package com.falconx.channelling.dao;

import lombok.Data;

@Data
public class IssuePrescriptionDAO {
    private long id;
    private int quantity;
    private boolean issued;
}
