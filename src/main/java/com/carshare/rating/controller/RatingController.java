package com.carshare.rating.controller;

import com.carshare.rating.entity.Rating;
import com.carshare.rating.service.RatingService;
import com.carshare.security.SecurityUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping("/booking/{bookingId}")
    public ResponseEntity<Rating> rateBooking(
            @PathVariable Long bookingId,
            @RequestBody Map<String, Object> body) {

        Integer stars = (Integer) body.get("stars");
        String comment = (String) body.get("comment");
        String raterRole = SecurityUtil.getCurrentUserRole().replace("ROLE_", "");

        Rating rating = ratingService.rateBooking(
                bookingId, SecurityUtil.getCurrentUserId(), raterRole, stars, comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(rating);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Rating>> getRatingsForUser(@PathVariable Long userId) {
        return ResponseEntity.ok(ratingService.getRatingsForUser(userId));
    }

    @GetMapping("/user/{userId}/average")
    public ResponseEntity<Double> getAverageRating(@PathVariable Long userId) {
        return ResponseEntity.ok(ratingService.getAverageRating(userId));
    }
}