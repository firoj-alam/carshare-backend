package com.carshare.rating.service;

import com.carshare.booking.entity.Booking;
import com.carshare.booking.entity.BookingStatus;
import com.carshare.booking.exception.BookingNotFoundException;
import com.carshare.booking.repository.BookingRepository;
import com.carshare.car.entity.Car;
import com.carshare.car.exception.CarNotFoundException;
import com.carshare.car.repository.CarRepository;
import com.carshare.rating.entity.Rating;
import com.carshare.rating.exception.DuplicateRatingException;
import com.carshare.rating.exception.InvalidRatingException;
import com.carshare.rating.repository.RatingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {

    private final RatingRepository ratingRepository;
    private final BookingRepository bookingRepository;
    private final CarRepository carRepository;

    public RatingService(RatingRepository ratingRepository, BookingRepository bookingRepository,
                          CarRepository carRepository) {
        this.ratingRepository = ratingRepository;
        this.bookingRepository = bookingRepository;
        this.carRepository = carRepository;
    }

    public Rating rateBooking(Long bookingId, Long raterId, String raterRole, Integer stars, String comment) {
        if (stars == null || stars < 1 || stars > 5) {
            throw new InvalidRatingException("Stars must be between 1 and 5.");
        }

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException(bookingId));

        if (booking.getStatus() != BookingStatus.COMPLETED) {
            throw new InvalidRatingException("You can only rate a COMPLETED booking.");
        }

        if (ratingRepository.existsByBookingIdAndRaterRole(bookingId, raterRole)) {
            throw new DuplicateRatingException("You have already rated this booking.");
        }

        Car car = carRepository.findById(booking.getCarId())
                .orElseThrow(() -> new CarNotFoundException(booking.getCarId()));

        Long rateeId;
        if ("OWNER".equals(raterRole)) {
            rateeId = booking.getDriverId();
        } else {
            rateeId = car.getOwnerId();
        }

        Rating rating = new Rating(bookingId, raterId, raterRole, rateeId, stars, comment);
        return ratingRepository.save(rating);
    }

    public List<Rating> getRatingsForUser(Long userId) {
        return ratingRepository.findByRateeId(userId);
    }

    public double getAverageRating(Long userId) {
        List<Rating> ratings = ratingRepository.findByRateeId(userId);
        if (ratings.isEmpty()) return 0.0;
        return ratings.stream().mapToInt(Rating::getStars).average().orElse(0.0);
    }
}