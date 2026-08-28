package com.parking.app.repository;

import com.parking.app.entity.ParkingToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParkingTokenRepository
        extends JpaRepository<ParkingToken, Long> {

    Optional<ParkingToken> findByTokenNumber(String tokenNumber);
}
