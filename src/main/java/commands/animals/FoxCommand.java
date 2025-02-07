package commands.animals;

import database.CommandUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.awt.*;
import java.time.Instant;

import api.RandomFoxAPI;
import static config.BotConstants.*;

public class FoxCommand {

    /**
     * Retrieves a URL of a random fox and handles it.
     * <p>
     * Handles <b>null</b> case.
     * </p>
     * <p>
     * Lastly, embeds the message and sends it.
     * </p>
     * @param event the discord event. Contains all information relating to discord.
     */
    public static void handleFoxCommand(SlashCommandInteraction event) {
        // Increment the usage count for the fox command in the database
        CommandUtils.incrementCommandUsage("fox");

        RandomFoxAPI.fetchRandomFox().thenAccept(randomFoxURL -> {
            if (randomFoxURL == null) {
                event.reply("Failed to fetch random fox image...").queue();
            } else {
                System.out.println("Random fox URL: " + randomFoxURL);
                EmbedBuilder embed = new EmbedBuilder();

                embed.setAuthor("Brownseal Howl", BOT_URL, BOT_IMAGE_URL);
                embed.setTitle("Hello world, this is a fox!");
                embed.setImage(randomFoxURL);
                embed.setColor(new Color(201, 176, 13));
                embed.setFooter("Courtesy of randomfox.ca");
                embed.setTimestamp(Instant.now());
                event.replyEmbeds(embed.build()).queue();
                embed.clear();
            }

        }).exceptionally(ex -> {
            event.reply("Exception occured fetching random fox image..." + ex.getMessage()).queue();
            return null; // Returns null to randomFoxURL
        });
    }

}
