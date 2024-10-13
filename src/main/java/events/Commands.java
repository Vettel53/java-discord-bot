package events;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.jetbrains.annotations.NotNull;

public class Commands extends ListenerAdapter {
    // Regarding Commands - >
    // Global Commands - Can be used anywhere: Any guid that your bot is in and also in DMs
    // May take up to an hour to register this command. Use Guild for testing
    // Guild Commands - They can only be used in a specific guild
    private JDA bot = null;
    public Commands() {

    }

    public void setBot(JDA bot) {
        this.bot = bot;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        Guild guild;
        guild = bot.getGuildById(1293823251004264448L); // TODO: Replace this with proper guild

        // Command names must be lowercase and must be one word (no spaces)
        if (guild != null) {
            guild.upsertCommand("test", "testing hard").queue();
            guild.upsertCommand("rank", "Search your Rocket League rank!")
                    .addOption(OptionType.STRING, "platform", "Account Platform", true)
                    .addOption(OptionType.STRING, "account", "Account Name", true)
                    .queue();
        } else {
            System.out.println("Guild not found.");
        }

        if (event.getName().equals("test")) {

            event.reply("Hello, test Command!").queue();

        } else if (event.getName().equals("rank")) {
            // Two sets of variables
            // OptionMapping -> Stores the OptionMapping objects for each option of the command
            // String -> Stores the value of the OptionMapping objects (ex: userPlatform or userName) in the event
            String platform;
            String name;
            OptionMapping userPlatform;
            OptionMapping userName;

            // Get options
            userPlatform = event.getOption("platform");
            userName = event.getOption("account");

            // Check if options are null, highly unlikely, but correct to check.
            if (userPlatform == null || userName == null) {
                event.reply("No option was found... Returning!").queue();
                return;
            }

            // get OptionMappings are proper strings
            platform = userPlatform.getAsString();
            name = userName.getAsString();

            event.reply("Platform : " + platform + " userName : " + name).queue();
        }

    }
}
