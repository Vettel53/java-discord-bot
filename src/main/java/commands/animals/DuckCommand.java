package commands.animals;

import api.RandomDuckAPI;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.awt.*;
import java.time.Instant;

import static config.BotConstants.BOT_IMAGE_URL;
import static config.BotConstants.BOT_URL;

public class DuckCommand {

    public static void handleDuckCommand(SlashCommandInteraction event, EmbedBuilder embed) {
        String randomDuckURL = RandomDuckAPI.fetchRandomDuck();

        if (randomDuckURL == null) {
            event.reply("Error fetching random Duck image...")
                    .queue();
        } else {
            embed.setAuthor("Brownseal Ducky", BOT_URL, BOT_IMAGE_URL);
            embed.setTitle("Hello world, this is a duck!");
            embed.setImage(randomDuckURL);
            embed.setColor(new Color(201, 176, 13));
            embed.setFooter("Courtesy of random-d.uk");
            embed.setTimestamp(Instant.now());
            event.replyEmbeds(embed.build()).queue();
            embed.clear();
        }
    }
    
}
