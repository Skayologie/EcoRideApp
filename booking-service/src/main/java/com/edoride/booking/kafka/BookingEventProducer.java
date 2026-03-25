package com.edoride.booking.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookingEventProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void publishBookingCreated(Long bookingId, Long rideId) {
        String message = String.format("Booking created: %d for ride: %d", bookingId, rideId);
        kafkaTemplate.send("booking-created-event", message);
        log.info("Published booking created event: {}", message);
    }
}
