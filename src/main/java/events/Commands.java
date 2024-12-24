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
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.time.Instant;

import static api.FortniteAPI.fetchPlayerStats;
import static api.RandomDogAPI.fetchRandomDog;
import static api.RandomFoxAPI.fetchRandomFox;

import services.PlayerCategorizerService;

public class Commands extends ListenerAdapter {
    // Regarding Commands - >
    // Global Commands - Can be used anywhere: Any guid that your bot is in and also in DMs
    // May take up to an hour to register this command. Use Guild for testing
    // Guild Commands - They can only be used in a specific guild
    private JDA bot = null;
    private final String botURL = "https://github.com/Vettel53/java-discord-bot";
    private final String botImageURL = "https://i.ibb.co/pjmHk7d/aas-thumbnail-fluffy-2048px-2048x-1.jpg";
    EmbedBuilder embed = new EmbedBuilder();
    public Commands() {

    }

    public void setBot(JDA bot) {
        this.bot = bot;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String subcommand;

        switch (event.getName()) {
            case "test":
                handleTestCommand(event);
                break;
            case "fortnite":
                // Get the subcommand name. Ex: "/fortnite stats", where stats is the subcommand
                subcommand = event.getSubcommandName();
                    if (subcommand == null) {
                        event.reply("Error occurred...").queue();
                        return;
                    } else if (subcommand.equals("stats")) {
                        handleRankCommand(event);
                    } else if (subcommand.equals("compare")) {
                        //TODO: Finish compare command
                        event.reply("Command not implemented yet...").queue();
                    }
                break;
            case "dog":
                handleDogCommand(event);
                break;
            case "fox":
                handleFoxCommand(event);
                break;
        }
    }

    public void handleTestCommand(SlashCommandInteraction event) {
        embed.setTitle("Test Command");
        embed.setDescription("This is a test command.");
        embed.setColor(new Color(255,255,255));
        embed.setFooter("Test");
        event.replyEmbeds(embed.build()).queue();
    }

    public void handleRankCommand(SlashCommandInteraction event) {
        // TODO: Document the reason for using discordUserName instead
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
                .thenAccept(result -> {
                    if (result == null) {
                        // Null case: The API call failed and no result was returned
                        event.reply("An unexpected error occurred while fetching player stats. Please try again later.").queue();
                        return;
                    }
                    PlayerStats playerStats = result;
                    String response = formatStatsResponse(playerStats);
                    embedStatsResponse(response, StringUserName, event);
                });
    }

//        fetchPlayerStats(StringUserName, StringPlaylist, StringPlatformType, StringTimeWindow, StringInput, new FortniteAPI.PlayerStatsCallback() {
//            public void onSuccess(PlayerStats playerStats) {
//                // Call formatStatsResponse to properly format the playerStats
//                String response = formatStatsResponse(playerStats);
//
//                // Call embedStatsResponse to properly embed the formatted response into Discord
//                embedStatsResponse(response, StringUserName, event);
//            }
//
//            public void onError(String errorMessage) {
//                // Handle the error, e.g., notify the user
//                event.reply("Error fetching player stats... " + errorMessage).queue();
//            }
//        });
//    }

    public void handleDogCommand(SlashCommandInteraction event) {
        String randomDogURL = fetchRandomDog();

        if (randomDogURL == null) {
            event.reply("Error fetching random dog image...")
                    .queue();
        } else {
            embed.setAuthor("Brownseal", botURL, botImageURL);
            embed.setTitle("Hello world, this is a dog!");
            embed.setImage(randomDogURL);
            embed.setColor(new Color(56, 52, 250));
            embed.setFooter("Courtesy of random.dog");
            embed.setTimestamp(Instant.now());
            event.replyEmbeds(embed.build()).queue();
            embed.clear();
        }
    }

    public void handleFoxCommand(SlashCommandInteraction event) {
        String randomFoxURL = fetchRandomFox();

        if (randomFoxURL == null) {
            event.reply("Error fetching random fox image...")
                   .queue();
        } else {
            embed.setAuthor("Brownseal Fluffy", botURL, botImageURL);
            embed.setTitle("Hello world, this is a fox!");
            embed.setImage(randomFoxURL);
            embed.setColor(new Color(255, 255, 0));
            embed.setFooter("Courtesy of randomfox.ca");
            embed.setTimestamp(Instant.now());
            event.replyEmbeds(embed.build()).queue();
            embed.clear();
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

        embed.setAuthor("Brownseal", botURL, "https://i.ibb.co/pjmHk7d/aas-thumbnail-fluffy-2048px-2048x-1.jpg" );
        embed.setTitle("Fortnite Stats for " + name);
        embed.setDescription(response);
        embed.setThumbnail("https://i.ibb.co/JsfKw6K/fortnite-Image.jpg");
        embed.setColor(new Color(56, 52, 250));
        embed.setFooter("Vettel53");
        embed.setTimestamp(Instant.now());
        event.replyEmbeds(embed.build()).queue();
        embed.clear();
    }

    public void addCommands() {
        Guild testingGuild;
        // This guild is my personal testing server, eventually global commands will be rolled out
        testingGuild = bot.getGuildById(1293823251004264448L);

        // Command names must be lowercase and must be one word (no spaces)
        if (testingGuild != null) {

            testingGuild.upsertCommand("test", "testing commands")
                    .queue();
            testingGuild.upsertCommand("dog", "Random dog photo!")
                    .queue();
            testingGuild.upsertCommand("fox", "Random fox photo!")
                    .queue();
            testingGuild.upsertCommand("fortnite", "Fortnite Commands!")
                    .addSubcommands(new SubcommandData ("stats", "Search a player's stats!")
                            // addOption names must be lowercase and one word
                            .addOption(OptionType.STRING, "name", "Account Name", true)

                            // Use addOptions instead of addOption for **predetermined** options
                            .addOptions(getPlaylistOption())
                            .addOptions(getPlatformOption())
                            .addOptions(getTimeWindowOption())
                            .addOptions(getImageOption())
                    )
                    .addSubcommands(new SubcommandData ("compare", "Compare two players against each other! (not working)")
                            .addOption(OptionType.STRING, "player1", "Player 1's username!", true)
                            .addOption(OptionType.STRING, "player2", "Player 2's username!", true)
                    )

                    .queue();
//            testingGuild.upsertCommand("fortnite", "Search your Fortnite stats!")
//                    // addOption names must be lowercase and one word
//                    .addOption(OptionType.STRING, "name", "Account Name", true)
//
//                    // Use addOptions instead of addOption for predetermined options
//                    .addOptions(getPlaylistOption())
//                    .addOptions(getPlatformOption())
//                    .addOptions(getTimeWindowOption())
//                    .addOptions(getImageOption())
//
//                    .queue();
        } else {
            System.out.println("Guild/Server not found.");
        }
    }

    // These OptionData methods allow pre-determined options to be shown to the user for some of the commands
    // This is primarily done as the fortnite-api only accepts certain parameters
    private OptionData getPlaylistOption() {
        return new OptionData(OptionType.STRING, "playlist", "Choose which playlist", false)
                .addChoice("overall", "overall")
                .addChoice("solo", "solo")
                .addChoice("duo", "duo")
                .addChoice("squad", "squad")
                .addChoice("limited-time-mode", "ltm");
    }

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