package com.parking.app.service.impl;

import com.parking.app.dto.BillingDTO;
import com.parking.app.entity.ParkingToken;
import com.parking.app.repository.ParkingTokenRepository;
import com.parking.app.service.BillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BillingServiceImpl
        implements BillingService {

    private final ParkingTokenRepository repository;

    @Override
    public BillingDTO getBill(
            String tokenNumber) {

        ParkingToken token = repository
                .findByTokenNumber(tokenNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Parking Token not found"));

        return BillingDTO.builder()
                .tokenNumber(
                        token.getTokenNumber())
                .vehicleNumber(
                        token.getVehicleNumber())
                .parkedMinutes(
                        token.getParkedMinutes())
                .billAmount(
                        token.getBillAmount())
                .build();
    }

    @Override
    public Double calculateBill(
            Long parkedMinutes) {

        if (parkedMinutes <= 60) {
            return 20.0;
        }

        return 20.0 +
                ((parkedMinutes - 60) * 0.50);
    }
}
