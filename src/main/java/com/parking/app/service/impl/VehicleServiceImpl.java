package com.parking.app.service.impl;

import com.parking.app.dto.VehicleDTO;
import com.parking.app.entity.ParkingToken;
import com.parking.app.entity.Vehicle;
import com.parking.app.repository.ParkingTokenRepository;
import com.parking.app.repository.VehicleRepository;
import com.parking.app.service.VehicleService;
import com.parking.app.util.TokenGeneratorUtil;
import com.parking.app.util.BillCalculatorUtil;
import com.parking.app.constants.AppConstants;
import com.parking.app.exception.ResourceNotFoundException;
import com.parking.app.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final ParkingTokenRepository tokenRepository;
    private final VehicleRepository vehicleRepository;
    private final ModelMapper modelMapper;

    @Override
    public VehicleDTO getVehicleDetails(String vehicleNumber) {
        Vehicle vehicle = vehicleRepository
                .findByVehicleNumber(vehicleNumber) // ✅ must exist in repository
                .orElseThrow(() ->
                        new ResourceNotFoundException("Vehicle not found"));

        return modelMapper.map(vehicle, VehicleDTO.class);
    }

    @Override
    public ParkingToken vehicleEntry(String vehicleNumber) {
        if (vehicleNumber == null || vehicleNumber.isBlank()) {
            throw new ValidationException("Vehicle number is mandatory");
        }

        ParkingToken token = ParkingToken.builder()
                .tokenNumber(TokenGeneratorUtil.generateToken())
                .vehicleNumber(vehicleNumber)
                .entryTime(LocalDateTime.now())
                .status(AppConstants.PARKED)
                .build();

        return tokenRepository.save(token);
    }

    @Override
    public ParkingToken vehicleExit(String tokenNumber) {
        ParkingToken token = tokenRepository
                .findByTokenNumber(tokenNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Token not found"));

        LocalDateTime exitTime = LocalDateTime.now();

        long parkedMinutes = Duration.between(
                token.getEntryTime(),
                exitTime
        ).toMinutes();

        Double amount = BillCalculatorUtil.calculateBill(parkedMinutes);

        token.setExitTime(exitTime);
        token.setParkedMinutes(parkedMinutes);
        token.setBillAmount(amount);
        token.setStatus(AppConstants.COMPLETED);

        return tokenRepository.save(token);
    }

    @Override
    public List<ParkingToken> getAllParkingRecords() {
        return tokenRepository.findAll();
    }

    @Override
    public ParkingToken findByTokenNumber(String tokenNumber) {
        return tokenRepository
                .findByTokenNumber(tokenNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Parking Token not found"));
    }
}
