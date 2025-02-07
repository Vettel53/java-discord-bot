package commands.animals;

import api.RandomCatAPI;
import database.CommandUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.awt.*;
import java.time.Instant;

import api.RandomDogAPI;
import static config.BotConstants.*;

public class DogCommand {

    /**
     * Retrieves a URL of a random dog and handles it.
     * <p>
     * Handles <b>null</b> case.
     * </p>
     * <p>
     * Lastly, embeds the message and sends it.
     * </p>
     * @param event the discord event. Contains all information relating to discord.
     */
    public static void handleDogCommand(SlashCommandInteraction event) {
        // Increment the usage count for the dog command in the database
        CommandUtils.incrementCommandUsage("dog");

        RandomDogAPI.fetchRandomDog().thenAccept(randomDogURL -> {
            if (randomDogURL == null) {
                event.reply("Failed to fetch random dog image...").queue();
            } else {
                System.out.println("Random dog URL: " + randomDogURL);
                EmbedBuilder embed = new EmbedBuilder();

                embed.setAuthor("Brownseal Woof", BOT_URL, BOT_IMAGE_URL);
                embed.setTitle("Hello world, this is a dog!");
                embed.setImage(randomDogURL);
                embed.setColor(new Color(56, 52, 250));
                embed.setFooter("Courtesy of random.dog");
                embed.setTimestamp(Instant.now());
                event.replyEmbeds(embed.build()).queue();
                embed.clear();
            }

        }).exceptionally(ex -> {
            event.reply("Exception occured fetching random dog image..." + ex.getMessage()).queue();
            return null; // Returns null to randomDogURL
        });
    }

}
