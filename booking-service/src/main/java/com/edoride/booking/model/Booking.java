package com.edoride.booking.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long rideId;
    private Long passengerId;
    private LocalDateTime bookingTime = LocalDateTime.now();
    
    @Enumerated(EnumType.STRING)
    private BookingStatus status = BookingStatus.PENDING;
}
