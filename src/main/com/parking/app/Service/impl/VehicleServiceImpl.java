package com.parking.app.service.impl;

import com.parking.app.entity.ParkingToken;
import com.parking.app.repository.ParkingTokenRepository;
import com.parking.app.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final ParkingTokenRepository tokenRepository;

    @Override
    public ParkingToken vehicleEntry(String vehicleNumber) {

        ParkingToken token = ParkingToken.builder()
                .tokenNumber("PK-" +
                        UUID.randomUUID()
                                .toString()
                                .substring(0, 8))
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
                        new RuntimeException("Token not found"));
    }

    private Double calculateAmount(
            Long parkedMinutes) {

        if (parkedMinutes <= 60) {
            return 20.0;
        }

        return 20.0 +
                ((parkedMinutes - 60) * 0.50);
    }
}
