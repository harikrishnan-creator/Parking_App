package com.parking.app.service;

import com.parking.app.entity.ParkingToken;
import com.parking.app.repository.ParkingTokenRepository;
import com.parking.app.service.impl.VehicleServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VehicleServiceImplTest {

    @InjectMocks
    private VehicleServiceImpl vehicleService;

    @Mock
    private ParkingTokenRepository tokenRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateVehicleEntryToken() {
        ParkingToken token = ParkingToken.builder()
                .id(1L)
                .tokenNumber("PK-123")
                .vehicleNumber("TN38AB1234")
                .entryTime(LocalDateTime.now())
                .status("PARKED")
                .build();

        when(tokenRepository.save(any()))
                .thenReturn(token);

        ParkingToken response = vehicleService.vehicleEntry("TN38AB1234");

        assertNotNull(response); // ✅ fixed variable name
        assertEquals("TN38AB1234", response.getVehicleNumber());
        assertEquals("PARKED", response.getStatus());

        verify(tokenRepository).save(any());
    }
}
