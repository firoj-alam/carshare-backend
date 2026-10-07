package com.carshare.booking.service;

import com.carshare.booking.dto.BookingRequest;
import com.carshare.booking.dto.BookingResponse;
import com.carshare.booking.entity.Booking;
import com.carshare.booking.entity.BookingStatus;
import com.carshare.booking.exception.BookingNotFoundException;
import com.carshare.booking.exception.BookingOverlapException;
import com.carshare.booking.exception.CarNotAvailableException;
import com.carshare.booking.exception.InvalidBookingStateException;
import com.carshare.booking.repository.BookingRepository;
import com.carshare.car.entity.Car;
import com.carshare.car.entity.CarStatus;
import com.carshare.car.exception.CarNotFoundException;
import com.carshare.car.repository.CarRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

	private final BookingRepository bookingRepository;
	private final CarRepository carRepository;
	private final com.carshare.payment.service.PaymentService paymentService;

	private final com.carshare.notification.service.NotificationService notificationService;

	public BookingService(BookingRepository bookingRepository, CarRepository carRepository,
	                       com.carshare.payment.service.PaymentService paymentService,
	                       com.carshare.notification.service.NotificationService notificationService) {
	    this.bookingRepository = bookingRepository;
	    this.carRepository = carRepository;
	    this.paymentService = paymentService;
	    this.notificationService = notificationService;
	}
    public BookingResponse requestBooking(Long driverId, BookingRequest request) {
        Car car = carRepository.findById(request.getCarId())
                .orElseThrow(() -> new CarNotFoundException(request.getCarId()));

        if (car.getStatus() != CarStatus.APPROVED || !car.isAvailable()) {
            throw new CarNotAvailableException(
                    "Car is not currently available for booking. Status: " + car.getStatus()
                            + ", Available: " + car.isAvailable());
        }

        List<Booking> overlaps = bookingRepository.findOverlappingBookings(
                request.getCarId(), request.getStartDate(), request.getEndDate());

        if (!overlaps.isEmpty()) {
            throw new BookingOverlapException(
                    "This car is already booked or requested for an overlapping date range.");
        }

        Booking booking = new Booking(
                request.getCarId(),
                driverId,
                request.getStartDate(),
                request.getEndDate(),
                request.getAgreedAmount()
        );

        Booking savedBooking = bookingRepository.save(booking);
        notificationService.notify(car.getOwnerId(), "New booking request for your car " + car.getBrand() + " " + car.getModel());
        return BookingResponse.fromEntity(savedBooking);
    }

    public BookingResponse getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException(id));
        return BookingResponse.fromEntity(booking);
    }

    public List<BookingResponse> getBookingsByDriver(Long driverId) {
        return bookingRepository.findByDriverId(driverId)
                .stream()
                .map(BookingResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<BookingResponse> getBookingsByCar(Long carId) {
        return bookingRepository.findByCarId(carId)
                .stream()
                .map(BookingResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public BookingResponse acceptBooking(Long id) {
        Booking booking = getBookingOrThrow(id);

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new InvalidBookingStateException(
                    "Only a PENDING booking can be accepted. Current status: " + booking.getStatus());
        }

        booking.setStatus(BookingStatus.ACCEPTED);
        notificationService.notify(booking.getDriverId(), "Your booking request #" + booking.getId() + " was accepted.");
        return BookingResponse.fromEntity(bookingRepository.save(booking));
    }

    public BookingResponse rejectBooking(Long id) {
        Booking booking = getBookingOrThrow(id);

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new InvalidBookingStateException(
                    "Only a PENDING booking can be rejected. Current status: " + booking.getStatus());
        }

        booking.setStatus(BookingStatus.REJECTED);
        notificationService.notify(booking.getDriverId(), "Your booking request #" + booking.getId() + " was declined.");
        return BookingResponse.fromEntity(bookingRepository.save(booking));
    }

    public BookingResponse activateBooking(Long id) {
        Booking booking = getBookingOrThrow(id);

        if (booking.getStatus() != BookingStatus.ACCEPTED) {
            throw new InvalidBookingStateException(
                    "Only an ACCEPTED booking can be activated. Current status: " + booking.getStatus());
        }

        booking.setStatus(BookingStatus.ACTIVE);
        return BookingResponse.fromEntity(bookingRepository.save(booking));
    }

    public BookingResponse completeBooking(Long id) {
        Booking booking = getBookingOrThrow(id);

        if (booking.getStatus() != BookingStatus.ACTIVE) {
            throw new InvalidBookingStateException(
                    "Only an ACTIVE booking can be completed. Current status: " + booking.getStatus());
        }

        booking.setStatus(BookingStatus.COMPLETED);
        Booking savedBooking = bookingRepository.save(booking);

        Car car = carRepository.findById(booking.getCarId())
                .orElseThrow(() -> new com.carshare.car.exception.CarNotFoundException(booking.getCarId()));

        paymentService.createPaymentForBooking(
                savedBooking.getId(),
                savedBooking.getCarId(),
                car.getOwnerId(),
                savedBooking.getDriverId(),
                savedBooking.getAgreedAmount()
        );

        return BookingResponse.fromEntity(savedBooking);
    }

    public BookingResponse cancelBooking(Long id) {
        Booking booking = getBookingOrThrow(id);

        if (booking.getStatus() != BookingStatus.PENDING && booking.getStatus() != BookingStatus.ACCEPTED) {
            throw new InvalidBookingStateException(
                    "Only a PENDING or ACCEPTED booking can be cancelled. Current status: " + booking.getStatus());
        }

        booking.setStatus(BookingStatus.CANCELLED);
        return BookingResponse.fromEntity(bookingRepository.save(booking));
    }
    
    public List<BookingResponse> getBookingsByOwner(Long ownerId) {
        List<Car> ownerCars = carRepository.findByOwnerId(ownerId);
        return ownerCars.stream()
                .flatMap(car -> bookingRepository.findByCarId(car.getId()).stream())
                .map(BookingResponse::fromEntity)
                .collect(Collectors.toList());
    }

    private Booking getBookingOrThrow(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new BookingNotFoundException(id));
    }
}