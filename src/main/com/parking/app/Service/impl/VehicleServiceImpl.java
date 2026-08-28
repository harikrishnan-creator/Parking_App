package com.parking.app.service.impl;

import com.parking.app.entity.ParkingToken;
import com.parking.app.repository.ParkingTokenRepository;
import com.parking.app.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.parking.app.util.TokenGeneratorUtil;
import com.parking.app.util.BillCalculatorUtil;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final ParkingTokenRepository tokenRepository;
    private final VehicleRepository vehicleRepository;
    private final ModelMapper modelMapper;

    @Override
    public VehicleDTO getVehicle(Long id) {

    Vehicle vehicle = vehicleRepository
            .findById(id)
            .orElseThrow(() ->
                    new RuntimeException("Vehicle not found"));

    return modelMapper.map(
            vehicle,
            VehicleDTO.class);
}

    @Override
    public ParkingToken vehicleEntry(String vehicleNumber) {

        if(vehicleNumber == null ||
       vehicleNumber.isBlank()) {

        throw new ValidationException(
                "Vehicle number is mandatory");
    }

        ParkingToken token = ParkingToken.builder()
                .setTokenNumber(TokenGeneratorUtil.generateToken())
                .vehicleNumber(vehicleNumber)
                .entryTime(LocalDateTime.now())
                .status("PARKED")
                .build();

        return tokenRepository.save(token);
    }

    @Override
    public ParkingToken vehicleExit(String tokenNumber) {

        ParkingToken token = tokenRepository
                .findByTokenNumber(tokenNumber)
                .orElseThrow(() ->
                        new RuntimeException("Token not found"));

        LocalDateTime exitTime = LocalDateTime.now();

        long parkedMinutes = Duration.between(
                token.getEntryTime(),
                exitTime)
                .toMinutes();

        Double amount = calculateAmount(
                parkedMinutes);

        token.setExitTime(exitTime);
        token.setParkedMinutes(parkedMinutes);
        token.setBillAmount(amount);
        token.setStatus("COMPLETED");

        return tokenRepository.save(token);
    }

    @Override
    public List<ParkingToken> getAllParkingRecords() {
        return tokenRepository.findAll();
    }

    @Override
    public ParkingToken findByTokenNumber(
            String tokenNumber) {

        return tokenRepository
                .findByTokenNumber(tokenNumber)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Parking Token not found"));
    }

   Double amount =
        BillCalculatorUtil.calculateBill(
                parkedMinutes);
}
