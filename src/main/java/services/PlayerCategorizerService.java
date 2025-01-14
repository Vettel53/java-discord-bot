package services;

import models.PlayerStats;

/**
 * This class provides a service for categorizing player statistics based on their kill-death ratio.
 */
public class PlayerCategorizerService {

    /**
     * Categorizes a player's kill-death ratio into a specific group.
     *
     * @param stats The player's statistics object containing the kill-death ratio.
     * @return A <b>String</b> representing the player's categorized group based on their kill-death ratio.
     */
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
