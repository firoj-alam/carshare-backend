package com.carshare.payment.service;

import com.carshare.payment.entity.Payment;
import com.carshare.payment.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Value("${app.platform-commission-rate}")
    private BigDecimal commissionRate;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment createPaymentForBooking(Long bookingId, Long carId, Long ownerId,
                                            Long driverId, BigDecimal totalAmount) {
        if (paymentRepository.existsByBookingId(bookingId)) {
            return paymentRepository.findByBookingId(bookingId).get();
        }

        BigDecimal commissionAmount = totalAmount.multiply(commissionRate)
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal ownerEarning = totalAmount.subtract(commissionAmount)
                .setScale(2, RoundingMode.HALF_UP);

        Payment payment = new Payment(bookingId, carId, ownerId, driverId,
                totalAmount, commissionRate, commissionAmount, ownerEarning);

        return paymentRepository.save(payment);
    }

    public List<Payment> getPaymentsByOwner(Long ownerId) {
        return paymentRepository.findByOwnerId(ownerId);
    }

    public List<Payment> getPaymentsByDriver(Long driverId) {
        return paymentRepository.findByDriverId(driverId);
    }
}