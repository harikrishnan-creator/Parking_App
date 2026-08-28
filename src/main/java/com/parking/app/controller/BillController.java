package com.parking.app.controller;

import com.parking.app.entity.ParkingToken;
import com.parking.app.repository.ParkingTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.parking.app.util.DateTimeUtil;

@RestController
@RequestMapping("/api/bills")
@RequiredArgsConstructor
public class BillController {

    private final ParkingTokenRepository repository;

    @GetMapping("/{tokenNumber}")
    public ParkingToken bill(
            @PathVariable String tokenNumber) {

        return repository.findByTokenNumber(tokenNumber)
                .orElseThrow();
    }
}
