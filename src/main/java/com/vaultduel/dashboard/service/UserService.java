package com.vaultduel.dashboard.service;

import com.vaultduel.dashboard.dto.UserResponse;
import com.vaultduel.dashboard.dto.UserUpdateRequest;
import com.vaultduel.dashboard.exception.ApiException;
import com.vaultduel.dashboard.model.Role;
import com.vaultduel.dashboard.model.User;
import com.vaultduel.dashboard.repository.MatchRecordRepository;
import com.vaultduel.dashboard.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** Admin-only user management: list everyone, change role, enable/disable, delete. */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final MatchRecordRepository matchRecordRepository;

    public UserService(UserRepository userRepository, MatchRecordRepository matchRecordRepository) {
        this.userRepository = userRepository;
        this.matchRecordRepository = matchRecordRepository;
    }

    public List<UserResponse> listAll() {
        return userRepository.findAll().stream().map(UserResponse::from).toList();
    }

    @Transactional
    public UserResponse update(Long userId, UserUpdateRequest req, String actingAdminEmail) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> ApiException.notFound("User not found."));

        boolean isSelf = user.getEmail().equalsIgnoreCase(actingAdminEmail);

        if (req.getRole() != null) {
            Role newRole;
            try {
                newRole = Role.valueOf(req.getRole().toUpperCase());
            } catch (IllegalArgumentException ex) {
                throw ApiException.badRequest("Role must be USER or ADMIN.");
            }
            if (isSelf && newRole != Role.ADMIN) {
                throw ApiException.badRequest("You can't demote your own account.");
            }
            user.setRole(newRole);
        }

        if (req.getEnabled() != null) {
            if (isSelf && !req.getEnabled()) {
                throw ApiException.badRequest("You can't disable your own account.");
            }
            user.setEnabled(req.getEnabled());
        }

        return UserResponse.from(userRepository.save(user));
    }

    @Transactional
    public void delete(Long userId, String actingAdminEmail) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> ApiException.notFound("User not found."));
        if (user.getEmail().equalsIgnoreCase(actingAdminEmail)) {
            throw ApiException.badRequest("You can't delete your own account.");
        }
        // MatchRecord.owner is a required (non-null) foreign key with no cascade configured,
        // so deleting a user who still has logged matches would otherwise fail as a raw DB
        // constraint violation (500). Failing clearly here instead tells the admin what to do.
        long ownedMatchCount = matchRecordRepository.countByOwner(user);
        if (ownedMatchCount > 0) {
            throw ApiException.badRequest(
                    "Can't delete this account: it still has " + ownedMatchCount +
                    " logged match(es). Delete those records first."
            );
        }
        userRepository.delete(user);
    }
}
