package services;

import models.PlayerStats;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.awt.*;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;

import static api.FortniteAPI.fetchPlayerStats;
import static config.BotConstants.BOT_IMAGE_URL;
import static config.BotConstants.BOT_URL;

/**
 * This class contains methods for comparing Fortnite player stats.
 */
public class FortniteComparePlayers {

    /**
     * Compares two Fortnite players' stats and determines the better one.
     *
     * @param event The Discord interaction event.
     * @param player1 The first player's username / platform-specific identifier.
     * @param player2 The second player's username / platform-specific identifier.
     */
    public static void comparePlayers(SlashCommandInteraction event, String player1, String player2) {
        // Validate inputs
        if (player1 == null || player2 == null) {
            event.reply("Sorry, we encountered an error. Please try again...").queue();
        }

        // Default values for API
        String playlist = "overall";
        String platformType = "epic";
        String timeWindow = "lifetime";
        String inputType = "none";

        // Fetch stats for both players asynchronously
        CompletableFuture<PlayerStats> player1StatsFuture = fetchPlayerStats(player1, playlist, platformType, timeWindow, inputType);
        CompletableFuture<PlayerStats> player2StatsFuture = fetchPlayerStats(player2, playlist, platformType, timeWindow, inputType);

        // Combine both async operations, ensure only runs after both async operations are complete
        CompletableFuture.allOf(player1StatsFuture, player2StatsFuture).thenRun(() -> {
                    try {
                        PlayerStats player1Stats = player1StatsFuture.get(); // difference between join and get?
                        PlayerStats player2Stats = player2StatsFuture.get();

                        if (player1Stats == null || player2Stats == null) {
                            event.reply("An unexpected error occurred while fetching player stats. Please try again later.").queue();
                            return;
                        }

                        determineBetterPlayer(event, player1Stats, player2Stats);
                    } catch (Exception e) {
                        event.reply("An error occurred while comparing players: " + e.getMessage()).queue();
                    }
        });

    }

    /**
     * Determines which player is better based on their Fortnite stats.
     * Uses "weights" to determine which statistics have more importance.
     *
     * @param event The Discord interaction event.
     * @param player1 The first player's stats.
     * @param player2 The second player's stats.
     */
    private static void determineBetterPlayer(SlashCommandInteraction event, PlayerStats player1, PlayerStats player2) {
        // Get player names
        String player1Name = player1.getPlayerName();
        String player2Name = player2.getPlayerName();
        String response;

        // Define weights of player stats (adjustable)
        double killsWeight = 0.4;
        double winsWeight = 0.3;
        double kdrWeight = 0.2;

        // Calculate skill ratings
        double player1Score = (player1.getKills() * killsWeight)
                + (player1.getWins() * winsWeight)
                + (player1.getKillDeathRatio() * kdrWeight);

        double player2Score = (player2.getKills() * killsWeight)
                + (player2.getWins() * winsWeight)
                + (player2.getKillDeathRatio() * kdrWeight);

        // Determine which player is "better" or handle tie
        if (player1Score > player2Score) {
            response = String.format(
                    "**Player Comparison**:\n" +
                            "\n🏆 **Winner: %s**\n" +
                            "🔹 **%s's Score:** %.2f\n" +
                            "🔹 **%s's Score:** %.2f",
                    player1Name, player1Name, player1Score, player2Name, player2Score
            );
            embedCompareResponse(event, response);
        } else if (player1Score < player2Score) {
            response = String.format(
                    "**Player Comparison**:\n" +
                            "\n🏆 **Winner: %s**\n" +
                            "🔹 **%s's Score:** %.2f\n" +
                            "🔹 **%s's Score:** %.2f",
                    player2Name, player2Name, player2Score, player1Name, player1Score
            );
            embedCompareResponse(event, response);
        } else if (player1Score == player2Score) { // Handle tie
            response = String.format(
                    "**Player Comparison**:\n" +
                            "\n🏆🏆 **Tie:**\n" +
                            "🔹 **%s's Score:** %.2f\n" +
                            "🔹 **%s's Score:** %.2f",
                    player2Name, player2Score, player1Name, player1Score
            );
            embedCompareResponse(event, response);
        }

    }

    /**
     * Sends the formatted response back to Discord as an embed.
     *
     * @param event The Discord interaction event.
     * @param response The formatted response string.
     */
    public static void embedCompareResponse(SlashCommandInteraction event, String response) {
        // Send the formatted response back to Discord as an embed
        EmbedBuilder embed = new EmbedBuilder();

        embed.setAuthor("Brownseal", BOT_URL, BOT_IMAGE_URL );
        embed.setTitle("Fortnite Stats Comparison");
        embed.setDescription(response);
        embed.setThumbnail("https://i.ibb.co/JsfKw6K/fortnite-Image.jpg");
        embed.setColor(new Color(111, 45, 150));
        embed.setFooter("Vettel53");
        embed.setTimestamp(Instant.now());
        event.replyEmbeds(embed.build()).queue();
        embed.clear();
    }

}
