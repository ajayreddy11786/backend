package com.vaultduel.dashboard.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class MatchRequest {

    private String roomCode;

    @NotBlank(message = "Opponent name is required")
    @Size(max = 100)
    private String opponentName;

    @Pattern(regexp = "\\d{4}", message = "Your code must be exactly 4 digits")
    private String yourSecretCode;

    @Pattern(regexp = "\\d{4}", message = "Opponent's code must be exactly 4 digits")
    private String opponentSecretCode;

    @Min(value = 1, message = "Guesses taken must be at least 1")
    private int guessesTaken;

    @NotBlank(message = "Result is required")
    @Pattern(regexp = "WON|LOST|DRAW", message = "Result must be WON, LOST, or DRAW")
    private String result;

    @NotNull(message = "Date played is required")
    @PastOrPresent(message = "Date played can't be in the future")
    private LocalDate playedAt;

    @Size(max = 1000)
    private String notes;

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

    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }

    public LocalDate getPlayedAt() { return playedAt; }
    public void setPlayedAt(LocalDate playedAt) { this.playedAt = playedAt; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
