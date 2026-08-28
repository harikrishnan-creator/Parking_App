package com.parking.app.mapper;

import com.parking.app.dto.VehicleDTO;
import com.parking.app.entity.Vehicle;
import org.springframework.stereotype.Component;

@Component
public class VehicleMapper {

    public VehicleDTO toDTO(Vehicle vehicle) {

        if (vehicle == null) {
            return null;
        }

        VehicleDTO dto = new VehicleDTO();

        dto.setId(vehicle.getId());
        dto.setVehicleNumber(vehicle.getVehicleNumber());
        dto.setVehicleType(vehicle.getVehicleType());
        dto.setOwnerName(vehicle.getOwnerName());

        return dto;
    }

    public Vehicle toEntity(VehicleDTO dto) {

        if (dto == null) {
            return null;
        }

        Vehicle vehicle = new Vehicle();

        vehicle.setId(dto.getId());
        vehicle.setVehicleNumber(dto.getVehicleNumber());
        vehicle.setVehicleType(dto.getVehicleType());
        vehicle.setOwnerName(dto.getOwnerName());

        return vehicle;
    }
}
