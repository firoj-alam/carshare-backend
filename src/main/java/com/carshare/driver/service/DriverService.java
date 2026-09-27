package com.carshare.driver.service;

import com.carshare.driver.dto.DriverRequest;
import com.carshare.driver.dto.DriverResponse;
import com.carshare.driver.entity.Driver;
import com.carshare.driver.exception.DriverNotFoundException;
import com.carshare.driver.exception.DuplicateEmailException;
import com.carshare.driver.exception.DuplicateLicenseException;
import com.carshare.driver.repository.DriverRepository;
import org.springframework.stereotype.Service;

@Service
public class DriverService {

    private final DriverRepository driverRepository;

    public DriverService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    public DriverResponse registerDriver(DriverRequest request) {
        if (driverRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException(request.getEmail());
        }

        if (driverRepository.existsByDrivingLicense(request.getDrivingLicense())) {
            throw new DuplicateLicenseException(request.getDrivingLicense());
        }

        Driver driver = new Driver(
                request.getName(),
                request.getEmail(),
                request.getMobile(),
                request.getPassword(), // NOTE: still plain text — fixed in Phase 5
                request.getDrivingLicense()
        );

        Driver savedDriver = driverRepository.save(driver);
        return DriverResponse.fromEntity(savedDriver);
    }

    public DriverResponse getDriverById(Long id) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new DriverNotFoundException(id));
        return DriverResponse.fromEntity(driver);
    }

    public DriverResponse updateDriver(Long id, DriverRequest request) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new DriverNotFoundException(id));

        driver.setName(request.getName());
        driver.setMobile(request.getMobile());
        // Email, password, and drivingLicense intentionally not updated
        // here — same reasoning as Owner: sensitive fields deserve a
        // dedicated, explicit flow rather than a generic update endpoint.

        Driver updatedDriver = driverRepository.save(driver);
        return DriverResponse.fromEntity(updatedDriver);
    }
}