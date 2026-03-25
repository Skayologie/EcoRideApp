package com.edoride.ride.controller;

import com.edoride.ride.model.Ride;
import com.edoride.ride.service.RideService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rides")
@RequiredArgsConstructor
public class RideController {
    private final RideService rideService;

    @PostMapping
    public ResponseEntity<Ride> createRide(@RequestBody Ride ride) {
        return ResponseEntity.ok(rideService.createRide(ride));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ride> getRide(@PathVariable Long id) {
        return ResponseEntity.ok(rideService.getRideById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Ride>> searchRides(
            @RequestParam String from,
            @RequestParam String to) {
        return ResponseEntity.ok(rideService.searchRides(from, to));
    }

    @GetMapping("/driver/{driverId}")
    public ResponseEntity<List<Ride>> getDriverRides(@PathVariable Long driverId) {
        return ResponseEntity.ok(rideService.getRidesByDriver(driverId));
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<Ride> completeRide(@PathVariable Long id) {
        return ResponseEntity.ok(rideService.completeRide(id));
    }
}
