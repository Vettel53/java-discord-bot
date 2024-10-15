package models;

public class PlayerStats {
    private String playerName;
    private int battlePassLevel;
    private int kills;
    private int wins;
    private int minutesPlayed;
    private double killDeathRatio;
    private String lastUpdatedData;

    public PlayerStats(String playerName, int battlePassLevel, int kills, int wins, int minutesPlayed, double killDeathRatio, String lastUpdatedData) {
        setPlayerName(playerName);
        setBattlePassLevel(battlePassLevel);
        setKills(kills);
        setWins(wins);
        setMinutesPlayed(minutesPlayed);
        setKillDeathRatio(killDeathRatio);
        setLastUpdatedData(lastUpdatedData);
    }

    // Public getters
    public String getPlayerName() {
        return playerName;
    }

    public int getBattlePassLevel() {
        return battlePassLevel;
    }

    public int getKills() {
        return kills;
    }

    public int getWins() {
        return wins;
    }

    public int getMinutesPlayed() {
        return minutesPlayed;
    }

    public double getKillDeathRatio() {
        return killDeathRatio;
    }

    public String getLastUpdatedData() {
        return lastUpdatedData;
    }

    // Private setters
    private void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    private void setBattlePassLevel(int battlePassLevel) {
        this.battlePassLevel = battlePassLevel;
    }

    private void setKills(int kills) {
        this.kills = kills;
    }

    private void setWins(int wins) {
        this.wins = wins;
    }

    private void setMinutesPlayed(int minutesPlayed) {
        this.minutesPlayed = minutesPlayed;
    }

    private void setKillDeathRatio(double killDeathRatio) {
        this.killDeathRatio = killDeathRatio;
    }

    private void setLastUpdatedData(String lastUpdatedData) {
        this.lastUpdatedData = lastUpdatedData;
    }

    // Constructor, getters, setters
}
