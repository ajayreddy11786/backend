package com.vaultduel.dashboard.service;

import com.vaultduel.dashboard.dto.AuthResponse;
import com.vaultduel.dashboard.dto.LoginRequest;
import com.vaultduel.dashboard.dto.RegisterRequest;
import com.vaultduel.dashboard.exception.ApiException;
import com.vaultduel.dashboard.model.Role;
import com.vaultduel.dashboard.model.User;
import com.vaultduel.dashboard.repository.UserRepository;
import com.vaultduel.dashboard.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil,
            AuthenticationManager authenticationManager
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw ApiException.conflict("An account with that email already exists.");
        }
        // Every self-registered account is a USER. Promotion to ADMIN happens only
        // through the admin dashboard, never at signup time.
        User user = new User(
                req.getFullName(),
                req.getEmail().toLowerCase(),
                passwordEncoder.encode(req.getPassword()),
                Role.USER
        );
        user = userRepository.save(user);
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return new AuthResponse(token, user.getId(), user.getFullName(), user.getEmail(), user.getRole().name());
    }

    public AuthResponse login(LoginRequest req) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getEmail().toLowerCase(), req.getPassword())
            );
        } catch (BadCredentialsException ex) {
            throw ex; // handled centrally -> 401
        }

        User user = userRepository.findByEmail(req.getEmail().toLowerCase())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!user.isEnabled()) {
            throw ApiException.forbidden("This account has been disabled.");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return new AuthResponse(token, user.getId(), user.getFullName(), user.getEmail(), user.getRole().name());
    }
}
