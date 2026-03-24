package com.edoride.user.kafka;

import com.edoride.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RideEventConsumer {
    private final UserService userService;

    @KafkaListener(topics = "ride-completed-event", groupId = "user-service")
    public void handleRideCompleted(String message) {
        log.info("Received ride completed event: {}", message);
        // Update driver reliability score logic here
    }
}
