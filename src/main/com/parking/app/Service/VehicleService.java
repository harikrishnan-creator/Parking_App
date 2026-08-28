package com.parking.app.service;

import com.parking.app.entity.ParkingToken;

public interface VehicleService {

    ParkingToken vehicleEntry(String vehicleNumber);

    ParkingToken vehicleExit(String tokenNumber);
}
