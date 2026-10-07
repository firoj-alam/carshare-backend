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
	private final com.carshare.notification.service.NotificationService notificationService;

	public AdminController(OwnerService ownerService, DriverService driverService, CarService carService,
	                        com.carshare.notification.service.NotificationService notificationService) {
	    this.ownerService = ownerService;
	    this.driverService = driverService;
	    this.carService = carService;
	    this.notificationService = notificationService;
	}

	@PutMapping("/owners/{id}/verify")
	public ResponseEntity<OwnerResponse> verifyOwner(@PathVariable Long id) {
	    OwnerResponse response = ownerService.verifyOwner(id);
	    notificationService.notify(id, "Your account has been verified. You can now use CarShare fully.");
	    return ResponseEntity.ok(response);
	}

	@PutMapping("/owners/{id}/reject")
	public ResponseEntity<OwnerResponse> rejectOwner(@PathVariable Long id) {
	    OwnerResponse response = ownerService.rejectOwner(id);
	    notificationService.notify(id, "Your account verification was rejected.");
	    return ResponseEntity.ok(response);
	}

	@PutMapping("/drivers/{id}/verify")
	public ResponseEntity<DriverResponse> verifyDriver(@PathVariable Long id) {
	    DriverResponse response = driverService.verifyDriver(id);
	    notificationService.notify(id, "Your account has been verified. You can now use CarShare fully.");
	    return ResponseEntity.ok(response);
	}

	@PutMapping("/drivers/{id}/reject")
	public ResponseEntity<DriverResponse> rejectDriver(@PathVariable Long id) {
	    DriverResponse response = driverService.rejectDriver(id);
	    notificationService.notify(id, "Your account verification was rejected.");
	    return ResponseEntity.ok(response);
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