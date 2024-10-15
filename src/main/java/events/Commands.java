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
        Guild guild;
        guild = bot.getGuildById(1293823251004264448L); // TODO: Replace this with proper guild

        // Command names must be lowercase and must be one word (no spaces)
        if (guild != null) {
            guild.upsertCommand("test", "testing hard").queue();
            guild.upsertCommand("rank", "Search your Rocket League rank!")
                    //.addOption(OptionType.STRING, "platform", "Account Platform", true)
                    .addOption(OptionType.STRING, "name", "Account Name", true)
                    .addOption(OptionType.STRING, "accounttype", "Account Platform", false)
                    .addOption(OptionType.STRING, "timewindow", "Season or Lifetime Stats", false)
                    .addOption(OptionType.STRING, "image", "Account Name", false)
                    .queue();
        } else {
            System.out.println("Guild not found.");
        }

        if (event.getName().equals("test")) {

            embed.setTitle("Test Command");
            embed.setDescription("This is a test command.");
            embed.setColor(new Color(255,255,255));
            embed.setFooter("Test");
            event.replyEmbeds(embed.build()).queue();
        } else if (event.getName().equals("rank")) {
            // Two sets of variables
            // OptionMapping -> Stores the OptionMapping objects for each option of the command
            // String -> Stores the value of the OptionMapping objects (ex: userPlatform or userName) in the event
            String name;
            OptionMapping userName;

            // Get options
            userName = event.getOption("name");

            // Check if options are null, highly unlikely, but correct to check.
            if (userName == null) {
                event.reply("No option was found... Returning!").queue();
                return;
            }

            // get OptionMappings userName as a proper string
            name = userName.getAsString();

            fetchPlayerStats(name, new FortniteAPI.PlayerStatsCallback() {
                public void onSuccess(PlayerStats playerStats) {
                    // Call formatStatsResponse to properly format the playerStats
                    String response = formatStatsResponse(playerStats);

                    // Call embedStatsResponse to properly embed the formatted response into Discord
                    embedStatsResponse(response, name, event);
                }

                public void onError(String errorMessage) {
                    // Handle the error, e.g., notify the user
                    event.reply("Error fetching player stats: " + errorMessage).queue();
                }
            });
        }
    }

    public String formatStatsResponse(PlayerStats playerStats) {
        // DOING: Implement this method to format the unformatted response into a Discord-friendly format

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
        // Send the formatted response back to Discord
        // TODO: Replace iconURL with proper forever URL
        embed.setAuthor("Brownseal", botURL, "https://i.ibb.co/pjmHk7d/aas-thumbnail-fluffy-2048px-2048x-1.jpg" );
        embed.setTitle("Fortnite Stats for " + name);
        embed.setDescription(response);
        embed.setThumbnail("https://i.ibb.co/JsfKw6K/fortnite-Image.jpg");
        embed.setColor(new Color(56, 52, 250));
        embed.setFooter("Vettel53");
        embed.setTimestamp(Instant.now());
        event.replyEmbeds(embed.build()).queue();
    }

}
