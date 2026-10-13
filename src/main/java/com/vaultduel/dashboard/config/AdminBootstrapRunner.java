package com.vaultduel.dashboard.config;

import com.vaultduel.dashboard.model.Role;
import com.vaultduel.dashboard.model.User;
import com.vaultduel.dashboard.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Creates one ADMIN account on first startup if no admin exists yet, using
 * app.admin.bootstrap-email / bootstrap-password (ADMIN_EMAIL / ADMIN_PASSWORD env vars).
 * Change this password immediately after first login.
 */
@Component
public class AdminBootstrapRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminBootstrapRunner.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.bootstrap-email}")
    private String bootstrapEmail;

    @Value("${app.admin.bootstrap-password}")
    private String bootstrapPassword;

    public AdminBootstrapRunner(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.existsByRole(Role.ADMIN)) {
            return;
        }
        User admin = new User(
                "Administrator",
                bootstrapEmail,
                passwordEncoder.encode(bootstrapPassword),
                Role.ADMIN
        );
        userRepository.save(admin);
        log.info("Bootstrapped initial admin account: {}", bootstrapEmail);
    }
}
