package com.carshare.booking.service;

import com.carshare.booking.dto.BookingRequest;
import com.carshare.booking.dto.BookingResponse;
import com.carshare.booking.entity.Booking;
import com.carshare.booking.entity.BookingStatus;
import com.carshare.booking.exception.BookingOverlapException;
import com.carshare.booking.exception.CarNotAvailableException;
import com.carshare.booking.exception.InvalidBookingStateException;
import com.carshare.booking.repository.BookingRepository;
import com.carshare.car.entity.Car;
import com.carshare.car.entity.CarStatus;
import com.carshare.car.repository.CarRepository;
import com.carshare.notification.service.NotificationService;
import com.carshare.payment.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CarRepository carRepository;

    @Mock
    private PaymentService paymentService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private BookingService bookingService;

    private final LocalDate start = LocalDate.of(2026, 11, 1);
    private final LocalDate end = LocalDate.of(2026, 11, 5);
    private final BigDecimal amount = new BigDecimal("3000.00");

    private Booking bookingWithStatus(BookingStatus status) {
        Booking b = new Booking(1L, 5L, start, end, amount);
        b.setId(7L);
        b.setStatus(status);
        return b;
    }

    private BookingRequest request() {
        BookingRequest r = new BookingRequest();
        r.setCarId(1L);
        r.setStartDate(start);
        r.setEndDate(end);
        r.setAgreedAmount(amount);
        return r;
    }

    @Test
    void acceptBooking_throwsException_whenNotPending() {
        when(bookingRepository.findById(7L)).thenReturn(Optional.of(bookingWithStatus(BookingStatus.COMPLETED)));

        assertThrows(InvalidBookingStateException.class, () -> bookingService.acceptBooking(7L));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void acceptBooking_movesToAccepted_whenPending() {
        when(bookingRepository.findById(7L)).thenReturn(Optional.of(bookingWithStatus(BookingStatus.PENDING)));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(inv -> inv.getArgument(0));

        BookingResponse response = bookingService.acceptBooking(7L);

        assertEquals(BookingStatus.ACCEPTED, response.getStatus());
    }

    @Test
    void requestBooking_throwsException_whenCarNotAvailable() {
        Car car = new Car(4L, "KA05AB1234", "Honda", "City", "Petrol", 2022); // PENDING, not available
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));

        assertThrows(CarNotAvailableException.class, () -> bookingService.requestBooking(5L, request()));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void requestBooking_throwsException_whenDatesOverlap() {
        Car car = new Car(4L, "KA05AB1234", "Honda", "City", "Petrol", 2022);
        car.setStatus(CarStatus.APPROVED);
        car.setAvailable(true);
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(bookingRepository.findOverlappingBookings(1L, start, end))
                .thenReturn(List.of(bookingWithStatus(BookingStatus.ACCEPTED)));

        assertThrows(BookingOverlapException.class, () -> bookingService.requestBooking(5L, request()));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void completeBooking_createsPayment_whenActive() {
        Booking booking = bookingWithStatus(BookingStatus.ACTIVE);
        Car car = new Car(4L, "KA05AB1234", "Honda", "City", "Petrol", 2022);
        when(bookingRepository.findById(7L)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenAnswer(inv -> inv.getArgument(0));
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));

        BookingResponse response = bookingService.completeBooking(7L);

        assertEquals(BookingStatus.COMPLETED, response.getStatus());
        verify(paymentService).createPaymentForBooking(7L, 1L, 4L, 5L, amount);
    }
}