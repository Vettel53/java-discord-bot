package commands;

import models.PlayerStats;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import services.FortniteComparePlayers;
import services.PlayerCategorizerService;

import java.awt.*;
import java.time.Instant;

import static api.FortniteAPI.fetchPlayerStats;
import static config.BotConstants.BOT_IMAGE_URL;
import static config.BotConstants.BOT_URL;
import static services.UserCommandCooldown.isUserOnCooldown;

public class FortniteCommand {

    /**
     * Handles the Fortnite stats parent command, processing different subcommands based on user input.
     * This method determines whether to fetch player stats or compare players, and delegates
     * to the appropriate handler method.
     *
     * @param event The SlashCommandInteraction event triggered by the user's command.
     *              This contains all the information about the command, including options and subcommands.
     */
    public void handleStatsCommand(SlashCommandInteraction event) {
    
        // Get the subcommand name
        String subCommandUsed = event.getSubcommandName();
    
        if (subCommandUsed == null) {
            event.reply("Error: Subcommand is required, but was not provided.").queue();
            return;
        }
    
        switch (subCommandUsed) {
            case "stats":
                // Validate the options
                OptionMapping userNameOption = event.getOption("name");
                if (userNameOption == null) {
                    event.reply("Sorry, we encountered an error. Please try again...").queue();
                    return;
                }
    
                // Extract OptionMapping's as Strings
                // If they are null, we will use the default values for the parameters as specified in the fortnite-api
                // https://dash.fortnite-api.com/endpoints/stats
                String userName = userNameOption.getAsString();
                String playlist = getOptionMappingAsString(event, "playlist", "overall");
                String platformType = getOptionMappingAsString(event, "accountplatform", "epic");
                String timeWindow = getOptionMappingAsString(event, "timewindow", "lifetime");
                String imageOption = getOptionMappingAsString(event, "image", "none");
                handleStatsSubcommand(event, userName, playlist, platformType, timeWindow, imageOption);
                break;
            case "compare":
                handleCompareSubCommand(event);
                break;
            default:
                event.reply("Error: Invalid subcommand. Please choose either 'stats' or 'compare'.").queue();
                break;
        }
    }

    /**
     * Retrieves the value of a specified option from the SlashCommandInteraction as a <b>String</b>.
     * If the option is not present, it returns/uses a default value as specified in the Fortnite API.
     *
     * @param event The SlashCommandInteraction event containing the command options
     * @param optionName The name of the option to retrieve.
     * @param defaultValue The default value to return if the option is not present.
     * @return The value of the specified option as a String, or the default value if the option is not present.
     */
    public String getOptionMappingAsString(SlashCommandInteraction event, String optionName, String defaultValue) {
        OptionMapping optionToConvert = event.getOption(optionName);
        if (optionToConvert != null) {
            return optionToConvert.getAsString();
        } else {
            return defaultValue;
        }
    }

    /**
     * Formats the player's statistics into a formatted <b>String</b> response.
     * This method takes a <b>PlayerStats object</b> and creates a formatted string
     * containing various statistics about the player's performance in Fortnite.
     *
     * @param playerStats The PlayerStats object containing the player's statistics.
     * @return A formatted string containing the player's statistics, including:
     *         - Player Class (Based off several statistics)
     *         - Battle Pass Level
     *         - Number of Kills
     *         - Number of Wins
     *         - Minutes Played
     *         - Kill/Death Ratio
     *         - Last Updated Date
     */
    public String formatStatsResponse(PlayerStats playerStats) {
        // TODO: Maybe implement more stats? Ask users for opinions
        String playerCategory = PlayerCategorizerService.categorizeKillDeath(playerStats);
    
        String formattedResponse = "**Player Class: **" + playerCategory + "\n\n"
                + "**Battle Pass Level: **" + playerStats.getBattlePassLevel() + "\n"
                + "**Kills: **" + playerStats.getKills() + "\n"
                + "**Wins: **" + playerStats.getWins() + "\n"
                + "**Minutes Played: **" + playerStats.getMinutesPlayed() + "\n"
                + String.format("**K/D Ratio: **%.2f%n", playerStats.getKillDeathRatio()) + "\n"
                + "**Last Updated: **" + playerStats.getLastUpdatedData();
    
        return formattedResponse;
    }

    /**
     * Embeds the formatted response back to Discord as an embed.
     *
     * @param response The formatted response containing player statistics.
     * @param name The name of the player for whom the statistics are being displayed.
     * @param event The SlashCommandInteraction event that triggered this method.
     */
    public void embedStatsResponse(String response, String name, SlashCommandInteraction event) {
        // Send the formatted response back to Discord as an embed
        EmbedBuilder embed = new EmbedBuilder();
    
        embed.setAuthor("Brownseal", BOT_URL, BOT_IMAGE_URL );
        embed.setTitle("Fortnite Stats for " + name);
        embed.setDescription(response);
        embed.setThumbnail("https://i.ibb.co/JsfKw6K/fortnite-Image.jpg");
        embed.setColor(new Color(56, 52, 250));
        embed.setFooter("Vettel53");
        embed.setTimestamp(Instant.now());
        event.replyEmbeds(embed.build()).queue();
        embed.clear();
    }

    /**
     * Handles the 'stats' subcommand of the Fortnite command. This method fetches and processes player statistics
     * based on the provided options.
     *
     * @param event The SlashCommandInteraction event triggered by the user's command.
     *              This contains all the information about the command, including options and subcommands.
     * @param userName The Fortnite username for which to fetch statistics.
     * @param playlist The Fortnite playlist for which to fetch statistics.
     * @param platformType The Fortnite/Console platform type for which to fetch statistics.
     * @param timeWindow The time window (Season/Lifetime) for which to fetch statistics.
     * @param imageOption The image (input device) option for the fetched statistics.
     */
    private void handleStatsSubcommand(SlashCommandInteraction event, String userName, String playlist, String platformType, String timeWindow, String imageOption) {
    
        fetchPlayerStats(userName, playlist, platformType, timeWindow, imageOption)
                // fetchPlayerStats returns a PlayerStats object which is stored in "result"
                .thenAccept(result -> {
                    if (result == null) {
                        // Null case: The API call failed and no result was returned
                        event.reply("An unexpected error occurred while fetching player stats. Please try again later.").queue();
                        return;
                    }
                    PlayerStats playerStats = result;
                    String response = formatStatsResponse(playerStats);
                    embedStatsResponse(response, userName, event);
                })
                .exceptionally(throwable -> {
                     // Error case: The API call failed and an exception occurred
                     event.reply("An error occurred while fetching player stats: " + throwable.getMessage()).queue();
                    return null;
                });
    }

    /**
     * Handles the 'compare' subcommand of the Fortnite command. This method fetches and compares player statistics
     * based on the provided options.
     *
     * @param event The SlashCommandInteraction event triggered by the user's command.
     *              This contains all the information about the command, including options and subcommands.
     */
    private void handleCompareSubCommand(SlashCommandInteraction event) {

        // Fetches player1/player2 names as Strings, null as default value to handle errors
        String player1 = getOptionMappingAsString(event, "player1", null);
        String player2 = getOptionMappingAsString(event, "player2", null);
    
        System.out.println(player1 + " " + player2);

        FortniteComparePlayers.comparePlayers(event, player1, player2);
    }

}
