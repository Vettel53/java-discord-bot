package services;

import java.util.HashMap;

public class UserCommandCooldown {
    private static final int COOLDOWN_DURATION_SECONDS = 4000; // 1 second
    private static final HashMap<String, Long> userCooldowns = new HashMap<>();

    public static boolean isUserOnCooldown(String userID) {
        long currentTime = System.currentTimeMillis();

        if (userCooldowns.containsKey(userID)) {
            long cooldownStart = userCooldowns.get(userID);
            long cooldownRemaining = COOLDOWN_DURATION_SECONDS - (currentTime - cooldownStart);

            if (cooldownRemaining > 0) {
                return true; // User is on cooldown
            }

            // If the cooldown has expired, remove the user from the cooldowns map.
            userCooldowns.remove(userID);
        }
        userCooldowns.put(userID, currentTime);
        return false; // User is not on cooldown
    }

    public static long getRemainingCooldown(String userID) {
        return userCooldowns.get(userID);
    }

}
