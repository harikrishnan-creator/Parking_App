package com.parking.app.mapper;

import com.parking.app.dto.BillingDTO;
import com.parking.app.entity.ParkingToken;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BillingMapper {
    BillingDTO toDTO(ParkingToken token);
}
