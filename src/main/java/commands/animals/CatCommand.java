package commands.animals;

import api.RandomCatAPI;
import database.CommandUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.awt.*;
import java.time.Instant;

import static config.BotConstants.BOT_IMAGE_URL;
import static config.BotConstants.BOT_URL;

public class CatCommand {

    /**
     * Retrieves a URL of a random cat and handles it.
     * <p>
     * Handles <b>null</b> case.
     * </p>
     * <p>
     * Lastly, embeds the message and sends it.
     * </p>
     * @param event the discord event. Contains all information relating to discord.
     */
    public static void handleCatCommand(SlashCommandInteraction event) {

        // Increment the usage count for the cat command in the database
        CommandUtils.incrementCommandUsage("cat");

        RandomCatAPI.fetchRandomCat().thenAccept(randomCatURL -> {
            if (randomCatURL == null) {
                event.reply("Failed to fetch random cat image...").queue();
            } else {
                System.out.println("Random cat URL: " + randomCatURL);
                EmbedBuilder embed = new EmbedBuilder();

                embed.setAuthor("Brownseal Meow", BOT_URL, BOT_IMAGE_URL);
                embed.setTitle("Hello world, this is a cat!");
                embed.setImage(randomCatURL);
                embed.setColor(new Color(241, 190, 169));
                embed.setFooter("Courtesy of developers.thecatapi.com");
                embed.setTimestamp(Instant.now());
                event.replyEmbeds(embed.build()).queue();
                embed.clear();
            }

        }).exceptionally(ex -> {
            event.reply("Exception occured fetching random cat image..." + ex.getMessage()).queue();
            return null; // Returns null to randomCatURL
        });

//        if (randomCatURL == null) {
//            event.reply("Error fetching random cat image...")
//                    .queue();
//        } else {
//            EmbedBuilder embed = new EmbedBuilder();
//
//            embed.setAuthor("Brownseal Meow", BOT_URL, BOT_IMAGE_URL);
//            embed.setTitle("Hello world, this is a cat!");
//            embed.setImage(randomCatURL);
//            embed.setColor(new Color(241, 190, 169));
//            embed.setFooter("Courtesy of developers.thecatapi.com");
//            embed.setTimestamp(Instant.now());
//            event.replyEmbeds(embed.build()).queue();
//            embed.clear();
//        }
    }
    
}
