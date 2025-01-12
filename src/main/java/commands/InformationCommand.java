package commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.awt.*;
import java.time.Instant;

import static config.BotConstants.BOT_IMAGE_URL;
import static config.BotConstants.BOT_URL;

public class InformationCommand {
    private static final long BYTES_TO_MB = 1024 * 1024; // Used to calculate the bytes to megabytes
    private final long maxMemory;
    private final long allocatedMemory;
    private final long freeMemory;
    private final int activeThreads;
    private final int availableProcessors;

    // Using instance variables to avoid "race conditions" and shared variables
    public InformationCommand() {
        this.maxMemory = Runtime.getRuntime().maxMemory();
        this.allocatedMemory = Runtime.getRuntime().totalMemory();
        this.freeMemory = Runtime.getRuntime().freeMemory();
        this.activeThreads = Thread.activeCount();
        this.availableProcessors = Runtime.getRuntime().availableProcessors();
    }

    public void handleInformationCommand(SlashCommandInteraction event) {

        try {
            String response = formatInformationResponse();

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

    public String formatInformationResponse() {

        String formattedResponse = "**OS Name: **" + System.getProperty("os.name") + "\n\n"
                + "**OS Version: **" + System.getProperty("os.version") + "\n"
                + "**Java Version: **" + System.getProperty("java.version")  + "\n"
                + "**Max Memory: **" + maxMemory / BYTES_TO_MB + " MB" + "\n"
                + "**Allocated Memory: **" + allocatedMemory / BYTES_TO_MB + " MB" + "\n"
                + "**Free Memory: **" + freeMemory / BYTES_TO_MB + " MB" + "\n"
                + "**Thread Count: **" + activeThreads + "\n"
                + "**Processor Count: **" + availableProcessors + "\n";

        return formattedResponse;
    }

}
