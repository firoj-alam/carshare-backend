package com.carshare.config;

import com.carshare.admin.entity.Admin;
import com.carshare.admin.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.default.email}")
    private String defaultAdminEmail;

    @Value("${admin.default.password}")
    private String defaultAdminPassword;

    @Value("${admin.default.name}")
    private String defaultAdminName;

    public DataSeeder(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (!adminRepository.existsByEmail(defaultAdminEmail)) {
            Admin admin = new Admin(
                    defaultAdminName,
                    defaultAdminEmail,
                    passwordEncoder.encode(defaultAdminPassword)
            );
            adminRepository.save(admin);
            System.out.println(">>> Default admin created: " + defaultAdminEmail);
        } else {
            System.out.println(">>> Admin already exists, skipping seeding.");
        }
    }
}