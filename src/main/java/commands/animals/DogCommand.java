package commands.animals;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.awt.*;
import java.time.Instant;

import api.RandomDogAPI;
import static config.BotConstants.*;

public class DogCommand {

    public static void handleDogCommand(SlashCommandInteraction event, EmbedBuilder embed) {
        String randomDogURL = RandomDogAPI.fetchRandomDog();

        if (randomDogURL == null) {
            event.reply("Error fetching random dog image...")
                    .queue();
        } else {
            embed.setAuthor("Brownseal Doggy", BOT_URL, BOT_IMAGE_URL);
            embed.setTitle("Hello world, this is a dog!");
            embed.setImage(randomDogURL);
            embed.setColor(new Color(56, 52, 250));
            embed.setFooter("Courtesy of random.dog");
            embed.setTimestamp(Instant.now());
            event.replyEmbeds(embed.build()).queue();
            embed.clear();
        }
    }

}
