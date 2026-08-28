package com.parking.app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "parking_token")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParkingToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tokenNumber;

    private String vehicleNumber;

    private LocalDateTime entryTime;

    private LocalDateTime exitTime;

    private Long parkedMinutes;

    private Double billAmount;

    private String status;
}
