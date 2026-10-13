package com.vaultduel.dashboard.controller;

import com.vaultduel.dashboard.dto.UserResponse;
import com.vaultduel.dashboard.dto.UserUpdateRequest;
import com.vaultduel.dashboard.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Admin-only: /api/admin/** is restricted to ROLE_ADMIN in SecurityConfig. */
@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserResponse> listAll() {
        return userService.listAll();
    }

    @PutMapping("/{id}")
    public UserResponse update(Authentication auth, @PathVariable Long id, @RequestBody UserUpdateRequest req) {
        return userService.update(id, req, auth.getName());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(Authentication auth, @PathVariable Long id) {
        userService.delete(id, auth.getName());
        return ResponseEntity.noContent().build();
    }
}
