package events;

import api.FortniteAPI;
import models.PlayerStats;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.time.Instant;

import static api.FortniteAPI.fetchPlayerStats;

public class Commands extends ListenerAdapter {
    // Regarding Commands - >
    // Global Commands - Can be used anywhere: Any guid that your bot is in and also in DMs
    // May take up to an hour to register this command. Use Guild for testing
    // Guild Commands - They can only be used in a specific guild
    private JDA bot = null;
    private final String botURL = "https://github.com/Vettel53/java-discord-bot";
    EmbedBuilder embed = new EmbedBuilder();
    public Commands() {

    }

    public void setBot(JDA bot) {
        this.bot = bot;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        addCommands();

        if (event.getName().equals("test")) {

            embed.setTitle("Test Command");
            embed.setDescription("This is a test command.");
            embed.setColor(new Color(255,255,255));
            embed.setFooter("Test");
            event.replyEmbeds(embed.build()).queue();
        } else if (event.getName().equals("rank")) {
            // Two sets of variables
            // OptionMapping -> Stores the OptionMapping objects for each option of the command
            // String -> Stores the String value of the OptionMapping objects (ex: userPlatform or userName) in the event
            String StringUserName, StringPlatformType, StringTimeWindow, StringInput;
            OptionMapping userName, accountPlatform, timeWindow, image;

            // Get options (Stored as OptionMapping)
            userName = event.getOption("name");
            accountPlatform = event.getOption("accountplatform");
            timeWindow = event.getOption("timewindow");
            image = event.getOption("image"); // image = device input

            // Check if options are null, highly unlikely, but correct to check.
            if (userName == null) {
                event.reply("Sorry, we encountered an error. Please try again...").queue();
                return;
            }

            // Get OptionMappings userName as a proper Strings and handle if they are null
            // If they are null, we will use the default values for the parameters as specified in the fortnite-api
            // https://dash.fortnite-api.com/endpoints/stats
            StringPlatformType = (accountPlatform != null) ? accountPlatform.getAsString() : "epic";
            StringTimeWindow = (timeWindow != null) ? timeWindow.getAsString() : "lifetime";
            StringInput = (image != null) ? image.getAsString() : "none";

            StringUserName = userName.getAsString();
            // We include all String conversions of the OptionData
            // This will allow us to build the fortnite-api call URL
            fetchPlayerStats(StringUserName, StringPlatformType, StringTimeWindow, StringInput, new FortniteAPI.PlayerStatsCallback() {
                public void onSuccess(PlayerStats playerStats) {
                    // Call formatStatsResponse to properly format the playerStats
                    String response = formatStatsResponse(playerStats);

                    // Call embedStatsResponse to properly embed the formatted response into Discord
                    embedStatsResponse(response, StringUserName, event);
                }

                public void onError(String errorMessage) {
                    // Handle the error, e.g., notify the user
                    event.reply("Error fetching player stats... " + errorMessage).queue();
                }
            });
        }
    }

    public String formatStatsResponse(PlayerStats playerStats) {
        // TODO: Maybe implement more stats? Ask users for opinions

        String formattedResponse = "**Player Name: **" + playerStats.getPlayerName() + "\n\n"
                + "**Battle Pass Level: **" + playerStats.getBattlePassLevel() + "\n"
                + "**Kills: **" + playerStats.getKills() + "\n"
                + "**Wins: **" + playerStats.getWins() + "\n"
                + "**Minutes Played: **" + playerStats.getMinutesPlayed() + "\n"
                + String.format("**K/D Ratio: **%.2f%n", playerStats.getKillDeathRatio()) + "\n"
                + "**Last Updated: **" + playerStats.getLastUpdatedData();

        return formattedResponse;
    }

    public void embedStatsResponse(String response, String name, SlashCommandInteractionEvent event) {
        // Send the formatted response back to Discord as an embed

        embed.setAuthor("Brownseal", botURL, "https://i.ibb.co/pjmHk7d/aas-thumbnail-fluffy-2048px-2048x-1.jpg" );
        embed.setTitle("Fortnite Stats for " + name);
        embed.setDescription(response);
        embed.setThumbnail("https://i.ibb.co/JsfKw6K/fortnite-Image.jpg");
        embed.setColor(new Color(56, 52, 250));
        embed.setFooter("Vettel53");
        embed.setTimestamp(Instant.now());
        event.replyEmbeds(embed.build()).queue();
    }

    public void addCommands() {
        Guild testingGuild;
        // This guild is my personal testing server, eventually global commands will be rolled out
        testingGuild = bot.getGuildById(1293823251004264448L);

        // Command names must be lowercase and must be one word (no spaces)
        if (testingGuild != null) {

            testingGuild.upsertCommand("test", "testing commands")
                    .queue();
            testingGuild.upsertCommand("rank", "Search your Rocket League rank!")
                    // addOption names must be lowercase and one word
                    .addOption(OptionType.STRING, "name", "Account Name", true)

                    // Use addOptions instead of addOption for predetermined options
                    .addOptions(getPlatformOption())
                    .addOptions(getTimeWindowOption())
                    .addOptions(getImageOption())

                    .queue();
        } else {
            System.out.println("Guild/Server not found.");
        }
    }

    // These OptionData methods allow pre-determined options to be shown to the user for some of the commands
    // This is primarily done as the fortnite-api only accepts certain parameters
    private OptionData getPlatformOption() {
        return new OptionData(OptionType.STRING, "accountplatform", "Choose your platform", false)
                .addChoice("epic games", "epic")
                .addChoice("psn", "psn")
                .addChoice("xbox", "xbl");
    }

    private OptionData getTimeWindowOption() {
        return new OptionData(OptionType.STRING, "timewindow", "Seasonal / Lifetime", false)
                .addChoice("seasonal", "season")
                .addChoice("lifetime", "lifetime");
    }

    private OptionData getImageOption() {
        return new OptionData(OptionType.STRING, "image", "Device Input", false)
                .addChoice("all", "all")
                .addChoice("keyboardMouse", "keyboardMouse")
                .addChoice("touchscreen", "touch")
                .addChoice("controller", "gamepad")
                .addChoice("none", "none");
    }
}
