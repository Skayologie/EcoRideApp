package com.edoride.booking.service;

import com.edoride.booking.client.RideServiceClient;
import com.edoride.booking.kafka.BookingEventProducer;
import com.edoride.booking.model.Booking;
import com.edoride.booking.model.BookingStatus;
import com.edoride.booking.repository.BookingRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingService {
    private final BookingRepository bookingRepository;
    private final RideServiceClient rideServiceClient;
    private final BookingEventProducer eventProducer;

    @CircuitBreaker(name = "rideService", fallbackMethod = "createBookingFallback")
    @Retry(name = "rideService")
    public Booking createBooking(Booking booking) {
        // Verify ride exists via Feign
        rideServiceClient.getRide(booking.getRideId());
        
        Booking saved = bookingRepository.save(booking);
        eventProducer.publishBookingCreated(saved.getId(), saved.getRideId());
        return saved;
    }

    public Booking createBookingFallback(Booking booking, Exception e) {
        log.error("Fallback: Unable to create booking", e);
        booking.setStatus(BookingStatus.CANCELLED);
        return booking;
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id).orElseThrow();
    }

    public List<Booking> getBookingsByPassenger(Long passengerId) {
        return bookingRepository.findByPassengerId(passengerId);
    }

    public Booking confirmBooking(Long bookingId) {
        Booking booking = getBookingById(bookingId);
        booking.setStatus(BookingStatus.CONFIRMED);
        return bookingRepository.save(booking);
    }
}
