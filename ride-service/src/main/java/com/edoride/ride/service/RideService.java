package com.edoride.ride.service;

import com.edoride.ride.model.Ride;
import com.edoride.ride.model.RideStatus;
import com.edoride.ride.repository.RideRepository;
import com.edoride.ride.kafka.RideEventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RideService {
    private final RideRepository rideRepository;
    private final RideEventProducer eventProducer;

    public Ride createRide(Ride ride) {
        return rideRepository.save(ride);
    }

    public Ride getRideById(Long id) {
        return rideRepository.findById(id).orElseThrow();
    }

    public List<Ride> searchRides(String fromCity, String toCity) {
        return rideRepository.findByFromCityAndToCity(fromCity, toCity);
    }

    public List<Ride> getRidesByDriver(Long driverId) {
        return rideRepository.findByDriverId(driverId);
    }

    public Ride completeRide(Long rideId) {
        Ride ride = getRideById(rideId);
        ride.setStatus(RideStatus.COMPLETED);
        Ride saved = rideRepository.save(ride);
        eventProducer.publishRideCompleted(rideId);
        return saved;
    }

    public Ride reduceSeats(Long rideId) {
        Ride ride = getRideById(rideId);
        ride.setAvailableSeats(ride.getAvailableSeats() - 1);
        return rideRepository.save(ride);
    }
}
