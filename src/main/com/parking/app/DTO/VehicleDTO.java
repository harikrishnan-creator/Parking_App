package com.parking.app.dto;

import lombok.Data;

@Data
public class VehicleDTO {

    private Long id;

    private String vehicleNumber;

    private String vehicleType;

    private String ownerName;
}
