package com.carshare.owner.dto;

import com.carshare.owner.entity.Owner;
import com.carshare.owner.entity.VerificationStatus;

import java.time.LocalDateTime;

public class OwnerResponse {

    private Long id;
    private String name;
    private String email;
    private String mobile;
    private VerificationStatus verificationStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public OwnerResponse() {
    }

    public static OwnerResponse fromEntity(Owner owner) {
        OwnerResponse response = new OwnerResponse();
        response.id = owner.getId();
        response.name = owner.getName();
        response.email = owner.getEmail();
        response.mobile = owner.getMobile();
        response.verificationStatus = owner.getVerificationStatus();
        response.createdAt = owner.getCreatedAt();
        response.updatedAt = owner.getUpdatedAt();
        return response;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public VerificationStatus getVerificationStatus() {
        return verificationStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}