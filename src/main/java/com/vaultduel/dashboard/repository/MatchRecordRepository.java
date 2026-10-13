package com.vaultduel.dashboard.repository;

import com.vaultduel.dashboard.model.MatchRecord;
import com.vaultduel.dashboard.model.MatchResult;
import com.vaultduel.dashboard.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRecordRepository extends JpaRepository<MatchRecord, Long> {
    List<MatchRecord> findByOwnerOrderByPlayedAtDesc(User owner);
    List<MatchRecord> findAllByOrderByPlayedAtDesc();
    long countByOwnerAndResult(User owner, MatchResult result);
    long countByOwner(User owner);
}
