package com.carshare.driver.service;

import com.carshare.driver.dto.DriverRequest;
import com.carshare.driver.dto.DriverResponse;
import com.carshare.driver.entity.Driver;
import com.carshare.driver.exception.DriverNotFoundException;
import com.carshare.driver.exception.DuplicateEmailException;
import com.carshare.driver.exception.DuplicateLicenseException;
import com.carshare.driver.repository.DriverRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DriverService {

    private final DriverRepository driverRepository;
    private final PasswordEncoder passwordEncoder;

    public DriverService(DriverRepository driverRepository, PasswordEncoder passwordEncoder) {
        this.driverRepository = driverRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public DriverResponse registerDriver(DriverRequest request) {
        if (driverRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException(request.getEmail());
        }

        if (driverRepository.existsByDrivingLicense(request.getDrivingLicense())) {
            throw new DuplicateLicenseException(request.getDrivingLicense());
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        Driver driver = new Driver(
                request.getName(),
                request.getEmail(),
                request.getMobile(),
                hashedPassword,
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
    
    public DriverResponse verifyDriver(Long id) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new DriverNotFoundException(id));
        driver.setVerificationStatus(com.carshare.driver.entity.VerificationStatus.APPROVED);
        return DriverResponse.fromEntity(driverRepository.save(driver));
    }

    public DriverResponse rejectDriver(Long id) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new DriverNotFoundException(id));
        driver.setVerificationStatus(com.carshare.driver.entity.VerificationStatus.REJECTED);
        return DriverResponse.fromEntity(driverRepository.save(driver));
    }

    public DriverResponse updateDriver(Long id, DriverRequest request) {
        Driver driver = driverRepository.findById(id)
                .orElseThrow(() -> new DriverNotFoundException(id));

        driver.setName(request.getName());
        driver.setMobile(request.getMobile());

        Driver updatedDriver = driverRepository.save(driver);
        return DriverResponse.fromEntity(updatedDriver);
    }
    
    public java.util.List<DriverResponse> getAllDrivers() {
        return driverRepository.findAll()
                .stream()
                .map(DriverResponse::fromEntity)
                .collect(java.util.stream.Collectors.toList());
    }
    
    
    
}