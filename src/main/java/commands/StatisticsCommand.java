package commands;

import database.CommandUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.awt.*;
import java.time.Instant;

import static config.BotConstants.BOT_IMAGE_URL;
import static config.BotConstants.BOT_URL;

public class StatisticsCommand {
    private static final long BYTES_TO_MB = 1024 * 1024; // Used to calculate the bytes to megabytes
    private final long maxMemory;
    private final long allocatedMemory;
    private final long freeMemory;
    private final int activeThreads;
    private final int availableProcessors;

    /**
     * Constructs an <b>StatisticsCommand object</b>, initializing system statistics
     * such as memory usage, active thread count, and available processors.
     * This constructor captures the current state of the system resources
     * to be used in generating information responses. Uses instance variables
     * to avoid "race conditions" and shared variables
     */
    public StatisticsCommand() {
        this.maxMemory = Runtime.getRuntime().maxMemory();
        this.allocatedMemory = Runtime.getRuntime().totalMemory();
        this.freeMemory = Runtime.getRuntime().freeMemory();
        this.activeThreads = Thread.activeCount();
        this.availableProcessors = Runtime.getRuntime().availableProcessors();
    }

    /**
     * Handles the statistics command by generating and sending an embedded message
     * containing system statistics and bot information to the Discord channel.
     *
     * @param event the SlashCommandInteraction event that triggered this command,
     *              providing context and methods to reply to the interaction.
     */
    public void handleStatisticCommand(SlashCommandInteraction event) {
        // Increment the usage count for the information command in the database
        CommandUtils.incrementCommandUsage("information");

        try {
            String response = generateStatisticResponse();

            EmbedBuilder embed = new EmbedBuilder();

            embed.setAuthor("Brownseal Statistics", BOT_URL, BOT_IMAGE_URL );
            embed.setTitle("Interesting Information!");
            embed.setDescription(response);
            embed.setColor(new Color(56, 52, 250));
            embed.setFooter("Vettel53");
            embed.setTimestamp(Instant.now());
            event.replyEmbeds(embed.build()).queue();
            embed.clear();
        } catch (Exception e) {
            event.reply("Error fetching information " + e.getMessage())
                    .queue();
        }

    }

    /**
     * Formats and returns a <b>String</b> containing system and environment information.
     * This includes details about the operating system, Java version, memory usage,
     * thread count, and processor count.
     *
     * @return A formatted <b>String</b> with system statistics and environment details.
     */
    public String generateStatisticResponse() {

        String formattedResponse = "**OS Name: **" + System.getProperty("os.name") + "\n\n"
                + "**OS Version: **" + System.getProperty("os.version") + "\n"
                + "**Java Version: **" + System.getProperty("java.version")  + "\n"
                + "**Max Memory: **" + maxMemory / BYTES_TO_MB + " MB" + "\n"
                + "**Allocated Memory: **" + allocatedMemory / BYTES_TO_MB + " MB" + "\n"
                + "**Free Memory: **" + freeMemory / BYTES_TO_MB + " MB" + "\n"
                + "**Thread Count: **" + activeThreads + "\n\n"
                + "**Dog Command Usages: **" + CommandUtils.fetchCommandUsageCount("dog") + "\n"
                + "**Cat Command Usages: **" + CommandUtils.fetchCommandUsageCount("cat") + "\n"
                + "**Fox Command Usages: **" + CommandUtils.fetchCommandUsageCount("fox") + "\n"
                + "**Duck Command Usages: **" + CommandUtils.fetchCommandUsageCount("duck") + "\n"
                + "**Information Command Usages: **" + CommandUtils.fetchCommandUsageCount("information") + "\n"
                + "**Avatar Command Usages: **" + CommandUtils.fetchCommandUsageCount("avatar") + "\n"
                + "**Fornite-compare Command Usages: **" + CommandUtils.fetchCommandUsageCount("fortnite-compare") + "\n"
                + "**Fortnite-stats Command Usages: **" + CommandUtils.fetchCommandUsageCount("fortnite-stats") + "\n";

        return formattedResponse;
    }

}
