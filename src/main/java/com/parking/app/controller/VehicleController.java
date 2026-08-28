package com.parking.app.controller;

import com.parking.app.entity.ParkingToken;
import com.parking.app.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping("/entry")
    public ParkingToken entry(
            @RequestParam String vehicleNumber) {

        return vehicleService.vehicleEntry(vehicleNumber);
    }

    @PostMapping("/exit")
    public ParkingToken exit(
            @RequestParam String tokenNumber) {

        return vehicleService.vehicleExit(tokenNumber);
    }
}
