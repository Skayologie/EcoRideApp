package com.edoride.ride.repository;

import com.edoride.ride.model.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RideRepository extends JpaRepository<Ride, Long> {
    List<Ride> findByFromCityAndToCity(String fromCity, String toCity);
    List<Ride> findByDriverId(Long driverId);
}
