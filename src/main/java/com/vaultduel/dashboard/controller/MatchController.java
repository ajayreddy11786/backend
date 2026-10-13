package com.vaultduel.dashboard.controller;

import com.vaultduel.dashboard.dto.MatchRequest;
import com.vaultduel.dashboard.dto.MatchResponse;
import com.vaultduel.dashboard.dto.StatsResponse;
import com.vaultduel.dashboard.service.MatchService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Shared by both dashboards: a USER only ever sees/modifies match records they logged
 * themselves (enforced in MatchService), and an ADMIN sees/modifies everyone's through
 * these same endpoints — no separate admin match API needed.
 */
@RestController
@RequestMapping("/api/matches")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping
    public List<MatchResponse> list(Authentication auth) {
        return matchService.listForUser(auth.getName());
    }

    @GetMapping("/stats")
    public StatsResponse stats(Authentication auth) {
        return matchService.statsForUser(auth.getName());
    }

    @PostMapping
    public ResponseEntity<MatchResponse> create(Authentication auth, @Valid @RequestBody MatchRequest req) {
        return ResponseEntity.ok(matchService.create(auth.getName(), req));
    }

    @PutMapping("/{id}")
    public MatchResponse update(Authentication auth, @PathVariable Long id, @Valid @RequestBody MatchRequest req) {
        return matchService.update(auth.getName(), id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(Authentication auth, @PathVariable Long id) {
        matchService.delete(auth.getName(), id);
        return ResponseEntity.noContent().build();
    }
}
