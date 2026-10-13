package com.vaultduel.dashboard.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;

/**
 * A logged Vault Duel game. Today these are entered by hand (play using the standalone
 * game, then record the result here); the field shape — roomCode, both secret codes,
 * guesses taken, result — is deliberately the same shape a live game server would write
 * automatically on game-over, so wiring real-time play back in later is just "insert a
 * MatchRecord when a room finishes" instead of a data-model change.
 */
@Entity
@Table(name = "match_record")
public class MatchRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The account this record belongs to — only this user (or an admin) can see/edit/delete it. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    /** Optional — set automatically once live multiplayer rooms feed this table directly. */
    @Column(length = 10)
    private String roomCode;

    @Column(nullable = false, length = 100)
    private String opponentName;

    @Column(length = 4)
    private String yourSecretCode;

    @Column(length = 4)
    private String opponentSecretCode;

    @Column(nullable = false)
    private int guessesTaken;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private MatchResult result;

    @Column(nullable = false)
    private LocalDate playedAt;

    @Column(length = 1000)
    private String notes;

    @Column(nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    private Instant updatedAt = Instant.now();

    public MatchRecord() {}

    @PreUpdate
    void onUpdate() {
        this.updatedAt = Instant.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getOwner() { return owner; }
    public void setOwner(User owner) { this.owner = owner; }

    public String getRoomCode() { return roomCode; }
    public void setRoomCode(String roomCode) { this.roomCode = roomCode; }

    public String getOpponentName() { return opponentName; }
    public void setOpponentName(String opponentName) { this.opponentName = opponentName; }

    public String getYourSecretCode() { return yourSecretCode; }
    public void setYourSecretCode(String yourSecretCode) { this.yourSecretCode = yourSecretCode; }

    public String getOpponentSecretCode() { return opponentSecretCode; }
    public void setOpponentSecretCode(String opponentSecretCode) { this.opponentSecretCode = opponentSecretCode; }

    public int getGuessesTaken() { return guessesTaken; }
    public void setGuessesTaken(int guessesTaken) { this.guessesTaken = guessesTaken; }

    public MatchResult getResult() { return result; }
    public void setResult(MatchResult result) { this.result = result; }

    public LocalDate getPlayedAt() { return playedAt; }
    public void setPlayedAt(LocalDate playedAt) { this.playedAt = playedAt; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
