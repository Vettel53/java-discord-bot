package services;

import models.PlayerStats;

public class PlayerCategorizerService {

    public static String categorizeKillDeath(PlayerStats stats) {
        double playerKD = stats.getKillDeathRatio();
        // TODO: Add more categories if needed.
        if (playerKD >= 10.0) {
            return "Untouchable";
        } else if (playerKD >= 5.0) {
            return "Apex Predator";
        } else if (playerKD >= 3.0) {
            return "Slayer";
        } else if (playerKD >= 1.5) {
            return "Grunt";
        } else if (playerKD >= 0.75) {
            return "Professional Spectator";
        } else {
            return "Unknown";
        }
    }

}
