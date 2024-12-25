package commands.animals;

import api.RandomCatAPI;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.awt.*;
import java.time.Instant;

import static config.BotConstants.BOT_IMAGE_URL;
import static config.BotConstants.BOT_URL;

public class CatCommand {

    public static void handleCatCommand(SlashCommandInteraction event, EmbedBuilder embed) {
        String randomCatURL = RandomCatAPI.fetchRandomCat();

        if (randomCatURL == null) {
            event.reply("Error fetching random cat image...")
                    .queue();
        } else {
            embed.setAuthor("Brownseal Meow", BOT_URL, BOT_IMAGE_URL);
            embed.setTitle("Hello world, this is a cat!");
            embed.setImage(randomCatURL);
            embed.setColor(new Color(241, 190, 169));
            embed.setFooter("Courtesy of developers.thecatapi.com");
            embed.setTimestamp(Instant.now());
            event.replyEmbeds(embed.build()).queue();
            embed.clear();
        }
    }
    
}
