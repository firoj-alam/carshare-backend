package com.carshare.owner.service;

import com.carshare.owner.dto.OwnerRequest;
import com.carshare.owner.dto.OwnerResponse;
import com.carshare.owner.entity.Owner;
import com.carshare.owner.exception.DuplicateEmailException;
import com.carshare.owner.exception.OwnerNotFoundException;
import com.carshare.owner.repository.OwnerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OwnerServiceTest {

    @Mock
    private OwnerRepository ownerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private OwnerService ownerService;

    @Test
    void registerOwner_throwsException_whenEmailAlreadyExists() {
        OwnerRequest request = new OwnerRequest();
        request.setEmail("taken@example.com");
        request.setName("Test");
        request.setMobile("9999999999");
        request.setPassword("password123");

        when(ownerRepository.existsByEmail("taken@example.com")).thenReturn(true);

        assertThrows(DuplicateEmailException.class, () -> ownerService.registerOwner(request));
        verify(ownerRepository, never()).save(any());
    }

    @Test
    void registerOwner_savesOwner_whenEmailIsNew() {
        OwnerRequest request = new OwnerRequest();
        request.setEmail("new@example.com");
        request.setName("New Owner");
        request.setMobile("9999999999");
        request.setPassword("password123");

        when(ownerRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("hashed_password");
        when(ownerRepository.save(any(Owner.class))).thenAnswer(invocation -> {
            Owner o = invocation.getArgument(0);
            o.setId(1L);
            return o;
        });

        OwnerResponse response = ownerService.registerOwner(request);

        assertEquals("New Owner", response.getName());
        assertEquals(1L, response.getId());
        verify(ownerRepository, times(1)).save(any(Owner.class));
    }

    @Test
    void getOwnerById_throwsException_whenNotFound() {
        when(ownerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(OwnerNotFoundException.class, () -> ownerService.getOwnerById(99L));
    }
}