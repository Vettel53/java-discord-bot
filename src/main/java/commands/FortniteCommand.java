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

    public String getOptionMappingAsString(SlashCommandInteraction event, String optionName, String defaultValue) {
        OptionMapping optionToConvert = event.getOption(optionName);
        if (optionToConvert != null) {
            return optionToConvert.getAsString();
        } else {
            return defaultValue;
        }
    }

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

    private void handleCompareSubCommand(SlashCommandInteraction event) {

        String player1 = getOptionMappingAsString(event, "player1", null);
        String player2 = getOptionMappingAsString(event, "player2", null);
        System.out.println(player1 + " " + player2);
        FortniteComparePlayers.comparePlayers(event, player1, player2);
    }

}
