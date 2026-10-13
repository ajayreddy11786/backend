package com.vaultduel.dashboard.service;

import com.vaultduel.dashboard.dto.ProfileUpdateRequest;
import com.vaultduel.dashboard.dto.UserResponse;
import com.vaultduel.dashboard.exception.ApiException;
import com.vaultduel.dashboard.model.User;
import com.vaultduel.dashboard.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfileService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse getProfile(String email) {
        return UserResponse.from(requireUser(email));
    }

    @Transactional
    public UserResponse updateProfile(String email, ProfileUpdateRequest req) {
        User user = requireUser(email);

        if (req.getFullName() != null && !req.getFullName().isBlank()) {
            user.setFullName(req.getFullName().trim());
        }

        if (req.getNewPassword() != null && !req.getNewPassword().isBlank()) {
            if (req.getCurrentPassword() == null || !passwordEncoder.matches(req.getCurrentPassword(), user.getPassword())) {
                throw ApiException.badRequest("Current password is incorrect.");
            }
            user.setPassword(passwordEncoder.encode(req.getNewPassword()));
        }

        return UserResponse.from(userRepository.save(user));
    }

    private User requireUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> ApiException.notFound("User not found."));
    }
}
