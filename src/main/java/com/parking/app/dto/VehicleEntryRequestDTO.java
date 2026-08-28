package com.parking.app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VehicleEntryRequestDTO {

    @NotBlank(message = "Vehicle number is required")
    private String vehicleNumber;

    @NotBlank(message = "Vehicle type is required")
    private String vehicleType;

    private String ownerName;
}
