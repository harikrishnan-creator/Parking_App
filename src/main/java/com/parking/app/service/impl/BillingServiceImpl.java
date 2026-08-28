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
    private final BillingMapper billingMapper;            

    @Override
    public BillingDTO getBill(
            String tokenNumber) {

        ParkingToken token = repository
                .findByTokenNumber(tokenNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Parking Token not found"));
/*
        return BillingDTO.builder()
                .tokenNumber(
                        token.getTokenNumber())
                .vehicleNumber(
                        token.getVehicleNumber())
                .parkedMinutes(
                        token.getParkedMinutes())
                .billAmount(
                        token.getBillAmount())
                .build(); */
            
         return billingMapper.toDTO(token);   
    }

    @Override
    public Double calculateBill(Long parkedMinutes) {

    return BillCalculatorUtil
            .calculateBill(parkedMinutes);
}
}
