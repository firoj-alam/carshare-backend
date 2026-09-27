package com.carshare.admin.controller;

import com.carshare.car.dto.CarResponse;
import com.carshare.car.service.CarService;
import com.carshare.driver.dto.DriverResponse;
import com.carshare.driver.service.DriverService;
import com.carshare.owner.dto.OwnerResponse;
import com.carshare.owner.service.OwnerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final OwnerService ownerService;
    private final DriverService driverService;
    private final CarService carService;

    public AdminController(OwnerService ownerService, DriverService driverService, CarService carService) {
        this.ownerService = ownerService;
        this.driverService = driverService;
        this.carService = carService;
    }

    @PutMapping("/owners/{id}/verify")
    public ResponseEntity<OwnerResponse> verifyOwner(@PathVariable Long id) {
        return ResponseEntity.ok(ownerService.verifyOwner(id));
    }

    @PutMapping("/owners/{id}/reject")
    public ResponseEntity<OwnerResponse> rejectOwner(@PathVariable Long id) {
        return ResponseEntity.ok(ownerService.rejectOwner(id));
    }

    @PutMapping("/drivers/{id}/verify")
    public ResponseEntity<DriverResponse> verifyDriver(@PathVariable Long id) {
        return ResponseEntity.ok(driverService.verifyDriver(id));
    }

    @PutMapping("/drivers/{id}/reject")
    public ResponseEntity<DriverResponse> rejectDriver(@PathVariable Long id) {
        return ResponseEntity.ok(driverService.rejectDriver(id));
    }

    @PutMapping("/cars/{id}/verify")
    public ResponseEntity<CarResponse> verifyCar(@PathVariable Long id) {
        return ResponseEntity.ok(carService.verifyCar(id));
    }

    @PutMapping("/cars/{id}/reject")
    public ResponseEntity<CarResponse> rejectCar(@PathVariable Long id) {
        return ResponseEntity.ok(carService.rejectCar(id));
    }
}