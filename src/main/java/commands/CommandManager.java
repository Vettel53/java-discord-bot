package commands;

import commands.animals.CatCommand;
import commands.animals.DogCommand;
import commands.animals.DuckCommand;
import commands.animals.FoxCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

import static services.UserCommandCooldown.getRemainingCooldown;
import static services.UserCommandCooldown.isUserOnCooldown;


/**
 * Manages and handles slash commands for the Discord bot.
 *
 * This class extends the ListenerAdapter to listen for slash command interactions.
 * Provides methods where you can edit to add commands to the bot
 * and handle different command types.
 *
 * @author Vettel53
 * @version 1.0
 */
public class CommandManager extends ListenerAdapter {
    // Regarding Commands - >
    // Global Commands - Can be used anywhere: Any guild that your bot is in and also in DMs
    // May take up to an hour to register this command. Use Guild for testing
    // Guild Commands - They can only be used in a specific guild
    private JDA bot = null;
    FortniteCommand fnCommand = new FortniteCommand();

    /**
     * Default constructor for CommandManager.
     */
    public CommandManager() {

    }

    /**
     * Sets the JDA bot instance for the CommandManager.
     *
     * @param bot The JDA bot instance.
     */
    public void setBot(JDA bot) {
        this.bot = bot;
    }

    /**
     * Handles slash command interactions.
     *
     * This method checks if the user is on cooldown, processes the command, and executes the appropriate command handler.
     *
     * @param event The SlashCommandInteractionEvent containing information about the command interaction.
     */
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        //event.deferReply().queue(); Use event.getHook to respond after this, still debating using

        String userID = event.getUser().getId();

        if (isUserOnCooldown(userID)) { // Check if user is on cooldown
            long remainingTime = System.currentTimeMillis() - getRemainingCooldown(userID);
            String response = String.format("Please wait %.1f second(s) before sending another command!", remainingTime / 1000.0);
            event.reply(response).queue();
            return;
        }

        CompletableFuture.runAsync(() -> {
            String subcommand;

            switch (event.getName()) {
                case "test":
                    DebugCommand.handleDebugCommand(event);
                    break;
                case "fortnite":
                    // Get the subcommand name. Ex: "/fortnite stats", where stats is the subcommand
                    subcommand = event.getSubcommandName();
                        if (subcommand == null) {
                            event.reply("Error occurred...").queue();
                            return;
                        }
                    fnCommand.handleStatsCommand(event);
                    break;
                case "dog":
                    DogCommand.handleDogCommand(event);
                    break;
                case "fox":
                    FoxCommand.handleFoxCommand(event);
                    break;
                case "duck":
                    DuckCommand.handleDuckCommand(event);
                    break;
                case "cat":
                    CatCommand.handleCatCommand(event);
                    break;
                case "statistics":
                    InformationCommand infoCommand = new InformationCommand();
                    infoCommand.handleInformationCommand(event);
                    break;
                case "avatar":
                    AvatarCommand.handleAvatarCommand(event);
                    break;
                // Add more commands here...
                default:
                    event.reply("Unknown command...").queue();
                    break;
            }
        });
    }

    // TODO: Document how to run this bot and maybe do global commands.
    /**
     * Adds commands to the bot.
     *
     * This method adds commands to a specific guild (testingGuild) for testing purposes.
     * It also adds subcommands to the "fortnite" command.
     */
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
            testingGuild.upsertCommand("duck", "Random duck photo!")
                            .queue();
            testingGuild.upsertCommand("cat", "Random cat photo!")
                            .queue();
            testingGuild.upsertCommand("statistics", "Statistics about Brownseal!")
                    .queue();
            testingGuild.upsertCommand("avatar", "Get your avatar!")
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