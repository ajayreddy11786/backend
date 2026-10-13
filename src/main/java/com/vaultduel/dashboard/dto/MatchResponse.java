package com.vaultduel.dashboard.dto;

import com.vaultduel.dashboard.model.MatchRecord;

import java.time.Instant;
import java.time.LocalDate;

public class MatchResponse {
    private Long id;
    private String roomCode;
    private String opponentName;
    private String yourSecretCode;
    private String opponentSecretCode;
    private int guessesTaken;
    private String result;
    private LocalDate playedAt;
    private String notes;
    private Long ownerId;
    private String ownerName;
    private Instant createdAt;
    private Instant updatedAt;

    public static MatchResponse from(MatchRecord m) {
        MatchResponse dto = new MatchResponse();
        dto.id = m.getId();
        dto.roomCode = m.getRoomCode();
        dto.opponentName = m.getOpponentName();
        dto.yourSecretCode = m.getYourSecretCode();
        dto.opponentSecretCode = m.getOpponentSecretCode();
        dto.guessesTaken = m.getGuessesTaken();
        dto.result = m.getResult().name();
        dto.playedAt = m.getPlayedAt();
        dto.notes = m.getNotes();
        dto.ownerId = m.getOwner().getId();
        dto.ownerName = m.getOwner().getFullName();
        dto.createdAt = m.getCreatedAt();
        dto.updatedAt = m.getUpdatedAt();
        return dto;
    }

    public Long getId() { return id; }
    public String getRoomCode() { return roomCode; }
    public String getOpponentName() { return opponentName; }
    public String getYourSecretCode() { return yourSecretCode; }
    public String getOpponentSecretCode() { return opponentSecretCode; }
    public int getGuessesTaken() { return guessesTaken; }
    public String getResult() { return result; }
    public LocalDate getPlayedAt() { return playedAt; }
    public String getNotes() { return notes; }
    public Long getOwnerId() { return ownerId; }
    public String getOwnerName() { return ownerName; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
}
