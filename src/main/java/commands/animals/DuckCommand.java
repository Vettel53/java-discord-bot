package commands.animals;

import api.RandomDogAPI;
import api.RandomDuckAPI;
import database.CommandUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.awt.*;
import java.time.Instant;

import static config.BotConstants.BOT_IMAGE_URL;
import static config.BotConstants.BOT_URL;

public class DuckCommand {

    /**
     * Retrieves a URL of a random duck and handles it.
     * <p>
     * Handles <b>null</b> case.
     * </p>
     * <p>
     * Lastly, embeds the message and sends it.
     * </p>
     * @param event the discord event. Contains all information relating to discord.
     */
    public static void handleDuckCommand(SlashCommandInteraction event) {
        // Increment the usage count for the duck command in the database
        CommandUtils.incrementCommandUsage("duck");

        RandomDuckAPI.fetchRandomDuck().thenAccept(randomDuckURL -> {
            if (randomDuckURL == null) {
                event.reply("Failed to fetch random duck image...").queue();
            } else {
                System.out.println("Random duck URL: " + randomDuckURL);
                EmbedBuilder embed = new EmbedBuilder();

                embed.setAuthor("Brownseal Quack", BOT_URL, BOT_IMAGE_URL);
                embed.setTitle("Hello world, this is a duck!");
                embed.setImage(randomDuckURL);
                embed.setColor(new Color(201, 176, 13));
                embed.setFooter("Courtesy of random-d.uk");
                embed.setTimestamp(Instant.now());
                event.replyEmbeds(embed.build()).queue();
                embed.clear();
            }

        }).exceptionally(ex -> {
            event.reply("Exception occured fetching random duck image..." + ex.getMessage()).queue();
            return null; // Returns null to randomDuckURL
        });
    }
    
}
