package commands;

import models.PlayerStats;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import services.PlayerCategorizerService;

import java.awt.*;
import java.time.Instant;

import static api.FortniteAPI.fetchPlayerStats;
import static config.BotConstants.BOT_IMAGE_URL;
import static config.BotConstants.BOT_URL;

public class FortniteCommand {

    public void handleStatsCommand(SlashCommandInteraction event, EmbedBuilder embed) {

        // Get the subcommand name that was used in the "fortnite" command
        String subCommandUsed = event.getSubcommandName();

        // Get options (OptionMapping) and ensure they are not null
        OptionMapping discordUserName = event.getOption("name");
        // Two sets of variables
        // OptionMapping -> Stores the OptionMapping objects for each option of the command
        // String -> Stores the String value of the OptionMapping objects (ex: userPlatform or userName) in the event
        String StringUserName, StringPlaylist, StringPlatformType, StringTimeWindow, StringInput;
        OptionMapping userName;

        // Get options (Stored as OptionMapping)
        userName = event.getOption("name");
        // Check if options are null, highly unlikely, but correct to check.
        if (userName == null) {
            event.reply("Sorry, we encountered an error. Please try again...").queue();
            return;
        }

        // Get OptionMappings userName as a proper Strings and handle if they are null
        // If they are null, we will use the default values for the parameters as specified in the fortnite-api
        // https://dash.fortnite-api.com/endpoints/stats
        StringPlaylist = getOptionMappingAsString(event, "playlist", "overall");
        StringPlatformType = getOptionMappingAsString(event, "accountplatform", "epic");
        StringTimeWindow = getOptionMappingAsString(event, "timewindow", "lifetime");
        StringInput = getOptionMappingAsString(event, "image", "none");
        StringUserName = userName.getAsString();

        fetchPlayerStats(StringUserName, StringPlaylist, StringPlatformType, StringTimeWindow, StringInput)
                // fetchPlayerStats returns a PlayerStats object which is stored in "result"
                .thenAccept(result -> {
                    if (result == null) {
                        // Null case: The API call failed and no result was returned
                        event.reply("An unexpected error occurred while fetching player stats. Please try again later.").queue();
                        return;
                    }
                    PlayerStats playerStats = result;
                    String response = formatStatsResponse(playerStats);

                    if (subCommandUsed == null) {
                        // Error occurred
                        event.reply(response).queue();
                    } else if (subCommandUsed.equals("stats")) {
                        embedStatsResponse(response, StringUserName, event, embed);
                    } else if (subCommandUsed.equals("compare")) {
                        // TODO
                    } else {
                        // Error occurred
                        event.reply("Unknown subcommand: " + subCommandUsed).queue();
                    }
                });
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

    public void embedStatsResponse(String response, String name, SlashCommandInteraction event, EmbedBuilder embed) {
        // Send the formatted response back to Discord as an embed

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

}
