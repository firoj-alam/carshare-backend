package com.carshare.rating.repository;

import com.carshare.rating.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {

    List<Rating> findByRateeId(Long rateeId);

    boolean existsByBookingIdAndRaterRole(Long bookingId, String raterRole);
}