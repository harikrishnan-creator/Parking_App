package com.parking.app.service;

import com.parking.app.entity.ParkingToken;

public interface VehicleService {

    ParkingToken vehicleEntry(String vehicleNumber);

    ParkingToken vehicleExit(String tokenNumber);

    List<ParkingToken> getAllParkingRecords();

    ParkingToken findByTokenNumber(String tokenNumber);

    VehicleDTO
