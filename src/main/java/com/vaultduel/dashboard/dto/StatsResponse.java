package com.vaultduel.dashboard.dto;

public class StatsResponse {
    private long totalMatches;
    private long wins;
    private long losses;
    private long draws;
    private double winRate; // 0.0–100.0, based on decided games (wins + losses)

    public StatsResponse(long totalMatches, long wins, long losses, long draws) {
        this.totalMatches = totalMatches;
        this.wins = wins;
        this.losses = losses;
        this.draws = draws;
        long decided = wins + losses;
        this.winRate = decided == 0 ? 0.0 : Math.round((wins * 1000.0) / decided) / 10.0;
    }

    public long getTotalMatches() { return totalMatches; }
    public long getWins() { return wins; }
    public long getLosses() { return losses; }
    public long getDraws() { return draws; }
    public double getWinRate() { return winRate; }
}
