package com.parking.app.service;

import com.parking.app.dto.BillingDTO;
import com.parking.app.entity.ParkingToken;
import com.parking.app.repository.ParkingTokenRepository;
import com.parking.app.service.impl.BillingServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(MockitoExtension.class)
class BillingServiceImplTest {

    @InjectMocks
    private BillingServiceImpl billingService;

    @Mock
    private ParkingTokenRepository repository;

    @Mock
    private BillingMapper billingMapper;   // <-- add this

    @Test
    void shouldReturnBillingDetails() {
        ParkingToken token = ParkingToken.builder()
                .tokenNumber("PK-111")
                .vehicleNumber("TN38AB1234")
                .parkedMinutes(120L)
                .billAmount(50.0)
                .build();

        BillingDTO dto = BillingDTO.builder()
                .tokenNumber("PK-111")
                .vehicleNumber("TN38AB1234")
                .parkedMinutes(120L)
                .billAmount(50.0)
                .build();

        when(repository.findByTokenNumber("PK-111"))
                .thenReturn(Optional.of(token));

        when(billingMapper.toDTO(token)).thenReturn(dto);  // <-- stub mapper

        BillingDTO result = billingService.getBill("PK-111");

        assertNotNull(result);
        assertEquals("PK-111", result.getTokenNumber());
    }

    @Test
    void shouldCalculateBill() {
        Double amount = billingService.calculateBill(120L);
        assertEquals(50.0, amount);
    }
}
