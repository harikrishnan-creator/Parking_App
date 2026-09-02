package com.parking.app.service;

import com.parking.app.dto.BillingDTO;
import com.parking.app.entity.ParkingToken;
import org.springframework.stereotype.Component;

@Component
public class BillingMapper {

    public BillingDTO toDTO(ParkingToken token) {
        if (token == null) {
            return null;
        }
        
        return BillingDTO.builder()
                .tokenNumber(token.getTokenNumber())
                .vehicleNumber(token.getVehicleNumber())
                .parkedMinutes(token.getParkedMinutes())
                .billAmount(token.getBillAmount())
                .build();
    }
}
