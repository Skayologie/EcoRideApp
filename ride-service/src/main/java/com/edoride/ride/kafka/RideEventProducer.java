package com.edoride.ride.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RideEventProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void publishRideCompleted(Long rideId) {
        String message = "Ride completed: " + rideId;
        kafkaTemplate.send("ride-completed-event", message);
        log.info("Published ride completed event: {}", message);
    }
}
