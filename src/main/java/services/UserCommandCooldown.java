package services;

import java.util.HashMap;

/**
 * This class manages user command cooldowns.
 */
public class UserCommandCooldown {
    private static final int COOLDOWN_DURATION_SECONDS = 1000; // 1 second
    private static final HashMap<String, Long> userCooldowns = new HashMap<>();

    /**
     * Checks if a user is on cooldown for executing a command.
     *
     * @param userID The unique identifier of the user.
     * @return True if the user is on cooldown, false otherwise.
     */
    public static boolean isUserOnCooldown(String userID) {
        long currentTime = System.currentTimeMillis();

        // Check if user is in the HashMap.
        if (userCooldowns.containsKey(userID)) {
            long cooldownStart = userCooldowns.get(userID);
            long cooldownRemaining = COOLDOWN_DURATION_SECONDS - (currentTime - cooldownStart);

            if (cooldownRemaining > 0) {
                return true; // User is on cooldown
            }

            // If the cooldown has expired, remove the user from the cooldowns map.
            userCooldowns.remove(userID);
        }
        userCooldowns.put(userID, currentTime); // Update the user to be on cooldown before returning
        return false; // User is not on cooldown
    }

    /**
     * Retrieves the remaining cooldown time for a user in milliseconds.
     *
     * @param userID The unique identifier of the user.
     * @return The remaining cooldown time in milliseconds. If the user is not on cooldown, returns 0.
     */

    public static long getRemainingCooldown(String userID) {
        return userCooldowns.get(userID);
    }

}
