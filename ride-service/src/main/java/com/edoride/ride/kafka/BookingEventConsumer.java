package com.edoride.ride.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import com.edoride.ride.service.RideService;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookingEventConsumer {
    private final RideService rideService;

    @KafkaListener(topics = "booking-created-event", groupId = "ride-service")
    public void handleBookingCreated(String message) {
        log.info("Received booking created event: {}", message);
        // Reduce available seats logic
    }
}
