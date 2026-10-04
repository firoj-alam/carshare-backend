package com.carshare.payment.controller;

import com.carshare.payment.entity.Payment;
import com.carshare.payment.service.PaymentService;
import com.carshare.security.SecurityUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/owner")
    public ResponseEntity<List<Payment>> getMyEarnings() {
        return ResponseEntity.ok(paymentService.getPaymentsByOwner(SecurityUtil.getCurrentUserId()));
    }

    @GetMapping("/driver")
    public ResponseEntity<List<Payment>> getMyPayments() {
        return ResponseEntity.ok(paymentService.getPaymentsByDriver(SecurityUtil.getCurrentUserId()));
    }
}