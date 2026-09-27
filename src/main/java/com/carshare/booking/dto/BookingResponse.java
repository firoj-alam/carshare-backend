package com.carshare.booking.dto;

import com.carshare.booking.entity.Booking;
import com.carshare.booking.entity.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class BookingResponse {

    private Long id;
    private Long carId;
    private Long driverId;
    private LocalDate startDate;
    private LocalDate endDate;
    private BookingStatus status;
    private BigDecimal agreedAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BookingResponse() {
    }

    public static BookingResponse fromEntity(Booking booking) {
        BookingResponse response = new BookingResponse();
        response.id = booking.getId();
        response.carId = booking.getCarId();
        response.driverId = booking.getDriverId();
        response.startDate = booking.getStartDate();
        response.endDate = booking.getEndDate();
        response.status = booking.getStatus();
        response.agreedAmount = booking.getAgreedAmount();
        response.createdAt = booking.getCreatedAt();
        response.updatedAt = booking.getUpdatedAt();
        return response;
    }

    public Long getId() {
        return id;
    }

    public Long getCarId() {
        return carId;
    }

    public Long getDriverId() {
        return driverId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public BigDecimal getAgreedAmount() {
        return agreedAmount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}