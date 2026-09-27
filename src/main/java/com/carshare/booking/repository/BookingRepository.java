package com.carshare.booking.repository;

import com.carshare.booking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByDriverId(Long driverId);

    List<Booking> findByCarId(Long carId);

    @Query("SELECT b FROM Booking b WHERE b.carId = :carId " +
           "AND b.status IN ('PENDING', 'ACCEPTED', 'ACTIVE') " +
           "AND b.startDate <= :endDate AND b.endDate >= :startDate")
    List<Booking> findOverlappingBookings(
            @Param("carId") Long carId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);
}