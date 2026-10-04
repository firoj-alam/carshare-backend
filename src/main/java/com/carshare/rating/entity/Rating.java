package com.carshare.rating.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "rating")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long bookingId;

    @Column(nullable = false)
    private Long raterId;

    @Column(nullable = false)
    private String raterRole; // "OWNER" or "DRIVER"

    @Column(nullable = false)
    private Long rateeId;

    @Column(nullable = false)
    private Integer stars;

    @Column(length = 500)
    private String comment;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Rating() {
    }

    public Rating(Long bookingId, Long raterId, String raterRole, Long rateeId, Integer stars, String comment) {
        this.bookingId = bookingId;
        this.raterId = raterId;
        this.raterRole = raterRole;
        this.rateeId = rateeId;
        this.stars = stars;
        this.comment = comment;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Long getBookingId() { return bookingId; }
    public Long getRaterId() { return raterId; }
    public String getRaterRole() { return raterRole; }
    public Long getRateeId() { return rateeId; }
    public Integer getStars() { return stars; }
    public String getComment() { return comment; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}