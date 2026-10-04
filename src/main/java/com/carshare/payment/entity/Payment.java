package com.carshare.payment.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long bookingId;

    @Column(nullable = false)
    private Long carId;

    @Column(nullable = false)
    private Long ownerId;

    @Column(nullable = false)
    private Long driverId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal commissionRate;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal commissionAmount;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal ownerEarningAmount;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Payment() {
    }

    public Payment(Long bookingId, Long carId, Long ownerId, Long driverId,
                   BigDecimal totalAmount, BigDecimal commissionRate,
                   BigDecimal commissionAmount, BigDecimal ownerEarningAmount) {
        this.bookingId = bookingId;
        this.carId = carId;
        this.ownerId = ownerId;
        this.driverId = driverId;
        this.totalAmount = totalAmount;
        this.commissionRate = commissionRate;
        this.commissionAmount = commissionAmount;
        this.ownerEarningAmount = ownerEarningAmount;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public Long getBookingId() { return bookingId; }
    public Long getCarId() { return carId; }
    public Long getOwnerId() { return ownerId; }
    public Long getDriverId() { return driverId; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public BigDecimal getCommissionRate() { return commissionRate; }
    public BigDecimal getCommissionAmount() { return commissionAmount; }
    public BigDecimal getOwnerEarningAmount() { return ownerEarningAmount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}