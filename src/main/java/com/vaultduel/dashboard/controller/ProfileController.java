package com.vaultduel.dashboard.controller;

import com.vaultduel.dashboard.dto.ProfileUpdateRequest;
import com.vaultduel.dashboard.dto.UserResponse;
import com.vaultduel.dashboard.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public UserResponse getProfile(Authentication auth) {
        return profileService.getProfile(auth.getName());
    }

    @PutMapping
    public UserResponse updateProfile(Authentication auth, @Valid @RequestBody ProfileUpdateRequest req) {
        return profileService.updateProfile(auth.getName(), req);
    }
}
