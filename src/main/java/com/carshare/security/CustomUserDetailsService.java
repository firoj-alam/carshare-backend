package com.carshare.security;

import com.carshare.driver.entity.Driver;
import com.carshare.driver.repository.DriverRepository;
import com.carshare.owner.entity.Owner;
import com.carshare.owner.repository.OwnerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final OwnerRepository ownerRepository;
    private final DriverRepository driverRepository;

    public CustomUserDetailsService(OwnerRepository ownerRepository, DriverRepository driverRepository) {
        this.ownerRepository = ownerRepository;
        this.driverRepository = driverRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Owner> ownerOpt = ownerRepository.findByEmail(email);
        if (ownerOpt.isPresent()) {
            Owner owner = ownerOpt.get();
            return new CustomUserDetails(owner.getId(), owner.getEmail(), owner.getPassword(), "ROLE_OWNER");
        }

        Optional<Driver> driverOpt = driverRepository.findByEmail(email);
        if (driverOpt.isPresent()) {
            Driver driver = driverOpt.get();
            return new CustomUserDetails(driver.getId(), driver.getEmail(), driver.getPassword(), "ROLE_DRIVER");
        }

        throw new UsernameNotFoundException("No user found with email: " + email);
    }
}