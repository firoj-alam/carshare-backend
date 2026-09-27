package com.carshare.security;

import com.carshare.admin.entity.Admin;
import com.carshare.admin.repository.AdminRepository;
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
    private final AdminRepository adminRepository;

    public CustomUserDetailsService(OwnerRepository ownerRepository,
                                     DriverRepository driverRepository,
                                     AdminRepository adminRepository) {
        this.ownerRepository = ownerRepository;
        this.driverRepository = driverRepository;
        this.adminRepository = adminRepository;
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

        Optional<Admin> adminOpt = adminRepository.findByEmail(email);
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            return new CustomUserDetails(admin.getId(), admin.getEmail(), admin.getPassword(), "ROLE_ADMIN");
        }

        throw new UsernameNotFoundException("No user found with email: " + email);
    }
}