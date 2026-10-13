package com.vaultduel.dashboard.service;

import com.vaultduel.dashboard.dto.MatchRequest;
import com.vaultduel.dashboard.dto.MatchResponse;
import com.vaultduel.dashboard.dto.StatsResponse;
import com.vaultduel.dashboard.exception.ApiException;
import com.vaultduel.dashboard.model.MatchRecord;
import com.vaultduel.dashboard.model.MatchResult;
import com.vaultduel.dashboard.model.Role;
import com.vaultduel.dashboard.model.User;
import com.vaultduel.dashboard.repository.MatchRecordRepository;
import com.vaultduel.dashboard.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Enforces the ownership rule in one place: a USER may only read/modify match records
 * they logged themselves; an ADMIN may read/modify anyone's. Controllers never bypass this.
 *
 * Class-level @Transactional(readOnly = true) keeps the Hibernate session open through DTO
 * mapping — MatchRecord.owner is a LAZY association and app.jpa.open-in-view is disabled,
 * so touching record.getOwner() after the repository call returns would otherwise throw
 * LazyInitializationException.
 */
@Service
@Transactional(readOnly = true)
public class MatchService {

    private final MatchRecordRepository matchRecordRepository;
    private final UserRepository userRepository;

    public MatchService(MatchRecordRepository matchRecordRepository, UserRepository userRepository) {
        this.matchRecordRepository = matchRecordRepository;
        this.userRepository = userRepository;
    }

    public List<MatchResponse> listForUser(String email) {
        User user = requireUser(email);
        List<MatchRecord> records = user.getRole() == Role.ADMIN
                ? matchRecordRepository.findAllByOrderByPlayedAtDesc()
                : matchRecordRepository.findByOwnerOrderByPlayedAtDesc(user);
        return records.stream().map(MatchResponse::from).toList();
    }

    public StatsResponse statsForUser(String email) {
        User user = requireUser(email);
        long total = matchRecordRepository.countByOwner(user);
        long wins = matchRecordRepository.countByOwnerAndResult(user, MatchResult.WON);
        long losses = matchRecordRepository.countByOwnerAndResult(user, MatchResult.LOST);
        long draws = matchRecordRepository.countByOwnerAndResult(user, MatchResult.DRAW);
        return new StatsResponse(total, wins, losses, draws);
    }

    @Transactional
    public MatchResponse create(String email, MatchRequest req) {
        User user = requireUser(email);
        MatchRecord record = new MatchRecord();
        applyRequest(record, req);
        record.setOwner(user);
        return MatchResponse.from(matchRecordRepository.save(record));
    }

    @Transactional
    public MatchResponse update(String email, Long matchId, MatchRequest req) {
        User user = requireUser(email);
        MatchRecord record = findAccessible(user, matchId);
        applyRequest(record, req);
        return MatchResponse.from(matchRecordRepository.save(record));
    }

    @Transactional
    public void delete(String email, Long matchId) {
        User user = requireUser(email);
        MatchRecord record = findAccessible(user, matchId);
        matchRecordRepository.delete(record);
    }

    // ---------- helpers ----------

    private MatchRecord findAccessible(User user, Long matchId) {
        MatchRecord record = matchRecordRepository.findById(matchId)
                .orElseThrow(() -> ApiException.notFound("Match record not found."));
        boolean isOwner = record.getOwner().getId().equals(user.getId());
        if (user.getRole() != Role.ADMIN && !isOwner) {
            throw ApiException.forbidden("You don't have access to this match record.");
        }
        return record;
    }

    private void applyRequest(MatchRecord record, MatchRequest req) {
        record.setRoomCode(blankToNull(req.getRoomCode()));
        record.setOpponentName(req.getOpponentName().trim());
        record.setYourSecretCode(blankToNull(req.getYourSecretCode()));
        record.setOpponentSecretCode(blankToNull(req.getOpponentSecretCode()));
        record.setGuessesTaken(req.getGuessesTaken());
        record.setResult(MatchResult.valueOf(req.getResult()));
        record.setPlayedAt(req.getPlayedAt());
        record.setNotes(blankToNull(req.getNotes()));
    }

    private String blankToNull(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }

    private User requireUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> ApiException.notFound("User not found."));
    }
}
