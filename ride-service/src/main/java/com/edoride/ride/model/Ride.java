package com.edoride.ride.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "rides")
@Data
public class Ride {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long driverId;
    private String fromCity;
    private String toCity;
    private LocalDateTime departureTime;
    private Integer availableSeats;
    private Double batteryAutonomy;
    
    @Enumerated(EnumType.STRING)
    private RideStatus status = RideStatus.AVAILABLE;
}
