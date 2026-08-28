package com.parking.app.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BillingDTO {

    private String tokenNumber;

    private String vehicleNumber;

    private LocalDateTime entryTime;

    private LocalDateTime exitTime;

    private Long parkedMinutes;

    private Double billAmount;

    private String status;
}
