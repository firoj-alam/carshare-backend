package com.carshare.owner.service;

import com.carshare.owner.dto.OwnerRequest;
import com.carshare.owner.dto.OwnerResponse;
import com.carshare.owner.entity.Owner;
import com.carshare.owner.exception.DuplicateEmailException;
import com.carshare.owner.exception.OwnerNotFoundException;
import com.carshare.owner.repository.OwnerRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class OwnerService {

    private final OwnerRepository ownerRepository;
    private final PasswordEncoder passwordEncoder;

    public OwnerService(OwnerRepository ownerRepository, PasswordEncoder passwordEncoder) {
        this.ownerRepository = ownerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public OwnerResponse registerOwner(OwnerRequest request) {
        if (ownerRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException(request.getEmail());
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        Owner owner = new Owner(
                request.getName(),
                request.getEmail(),
                request.getMobile(),
                hashedPassword
        );

        Owner savedOwner = ownerRepository.save(owner);
        return OwnerResponse.fromEntity(savedOwner);
    }

    public OwnerResponse getOwnerById(Long id) {
        Owner owner = ownerRepository.findById(id)
                .orElseThrow(() -> new OwnerNotFoundException(id));
        return OwnerResponse.fromEntity(owner);
    }

    public OwnerResponse updateOwner(Long id, OwnerRequest request) {
        Owner owner = ownerRepository.findById(id)
                .orElseThrow(() -> new OwnerNotFoundException(id));

        owner.setName(request.getName());
        owner.setMobile(request.getMobile());

        Owner updatedOwner = ownerRepository.save(owner);
        return OwnerResponse.fromEntity(updatedOwner);
    }
}