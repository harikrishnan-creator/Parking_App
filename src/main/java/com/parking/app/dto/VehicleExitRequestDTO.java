package com.parking.app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VehicleExitRequestDTO {

    @NotBlank(message = "Token number is required")
    private String Token;

    private String Vehicle;

}
