package com.edoride.booking.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ride-service")
public interface RideServiceClient {
    
    @GetMapping("/api/rides/{id}")
    Object getRide(@PathVariable Long id);
}
