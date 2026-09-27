package com.carshare.owner.service;

import com.carshare.owner.dto.OwnerRequest;
import com.carshare.owner.dto.OwnerResponse;
import com.carshare.owner.entity.Owner;
import com.carshare.owner.entity.VerificationStatus;
import com.carshare.owner.exception.DuplicateEmailException;
import com.carshare.owner.exception.OwnerNotFoundException;
import com.carshare.owner.repository.OwnerRepository;
import org.springframework.stereotype.Service;

@Service
public class OwnerService {

    private final OwnerRepository ownerRepository;

    public OwnerService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    public OwnerResponse registerOwner(OwnerRequest request) {
        if (ownerRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException(request.getEmail());
        }

        Owner owner = new Owner(
                request.getName(),
                request.getEmail(),
                request.getMobile(),
                request.getPassword() // NOTE: still plain text — fixed in Phase 5
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
        // Email and password intentionally not updated here to keep this
        // step simple. Changing email/password safely (re-verification,
        // re-hashing) is a more sensitive flow we will handle explicitly
        // in a later phase rather than lumping it into a generic update.

        Owner updatedOwner = ownerRepository.save(owner);
        return OwnerResponse.fromEntity(updatedOwner);
    }
}