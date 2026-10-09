package com.carshare.common.exception;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
	private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<ErrorResponse> handleNoResource(
	        NoResourceFoundException ex, HttpServletRequest request) {
	    return buildResponse(HttpStatus.NOT_FOUND, "Resource not found", request);
	}
    // ===== Owner Exceptions =====
	

    @ExceptionHandler(com.carshare.owner.exception.DuplicateEmailException.class)
    public ResponseEntity<ErrorResponse> handleOwnerDuplicateEmail(
            com.carshare.owner.exception.DuplicateEmailException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    @ExceptionHandler(com.carshare.owner.exception.OwnerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOwnerNotFound(
            com.carshare.owner.exception.OwnerNotFoundException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    // ===== Driver Exceptions =====

    @ExceptionHandler(com.carshare.driver.exception.DuplicateEmailException.class)
    public ResponseEntity<ErrorResponse> handleDriverDuplicateEmail(
            com.carshare.driver.exception.DuplicateEmailException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    @ExceptionHandler(com.carshare.driver.exception.DuplicateLicenseException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateLicense(
            com.carshare.driver.exception.DuplicateLicenseException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    @ExceptionHandler(com.carshare.driver.exception.DriverNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDriverNotFound(
            com.carshare.driver.exception.DriverNotFoundException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    // ===== Car Exceptions =====

    @ExceptionHandler(com.carshare.car.exception.CarNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCarNotFound(
            com.carshare.car.exception.CarNotFoundException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(com.carshare.car.exception.DuplicateRegistrationException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateRegistration(
            com.carshare.car.exception.DuplicateRegistrationException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    @ExceptionHandler(com.carshare.car.exception.InvalidCarStateException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCarState(
            com.carshare.car.exception.InvalidCarStateException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    // ===== Booking Exceptions =====

    @ExceptionHandler(com.carshare.booking.exception.BookingNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBookingNotFound(
            com.carshare.booking.exception.BookingNotFoundException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(com.carshare.booking.exception.InvalidBookingStateException.class)
    public ResponseEntity<ErrorResponse> handleInvalidBookingState(
            com.carshare.booking.exception.InvalidBookingStateException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(com.carshare.booking.exception.CarNotAvailableException.class)
    public ResponseEntity<ErrorResponse> handleCarNotAvailable(
            com.carshare.booking.exception.CarNotAvailableException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(com.carshare.booking.exception.BookingOverlapException.class)
    public ResponseEntity<ErrorResponse> handleBookingOverlap(
            com.carshare.booking.exception.BookingOverlapException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    // ===== Validation Errors (@Valid) =====

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        Map<String, String> fieldErrors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());
        }

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Validation failed for one or more fields",
                request.getRequestURI(),
                fieldErrors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // ===== Security Exceptions =====

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(
            BadCredentialsException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Invalid email or password", request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(
            AccessDeniedException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.FORBIDDEN, "You do not have permission to access this resource", request);
    }

    // ===== Fallback — anything else uncaught =====

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, HttpServletRequest request) {
    	log.error("Unexpected error on {}", request.getRequestURI(), ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", request);
    }

    // ===== Helper method =====

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message, HttpServletRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(errorResponse);
    }
    
    @ExceptionHandler(com.carshare.rating.exception.InvalidRatingException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRating(
            com.carshare.rating.exception.InvalidRatingException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(com.carshare.rating.exception.DuplicateRatingException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateRating(
            com.carshare.rating.exception.DuplicateRatingException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), request);
    }
}