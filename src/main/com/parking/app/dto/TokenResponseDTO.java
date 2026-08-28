package com.parking.app.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TokenResponseDTO {

    private String tokenNumber;

    private String vehicleNumber;

    private String vehicleType;

    private LocalDateTime entryTime;

    private String status;
}
