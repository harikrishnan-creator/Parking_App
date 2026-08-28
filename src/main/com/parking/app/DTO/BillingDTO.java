package com.parking.app.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BillingDTO {

    private String tokenNumber;
    private String vehicleNumber;
    private Long parkedMinutes;
    private Double billAmount;
}
