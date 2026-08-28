package com.parking.app.mapper;

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
                .entryTime(token.getEntryTime())
                .exitTime(token.getExitTime())
                .parkedMinutes(token.getParkedMinutes())
                .billAmount(token.getBillAmount())
                .status(token.getStatus())
                .build();
    }
}
