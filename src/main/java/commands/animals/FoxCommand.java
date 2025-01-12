package commands.animals;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.awt.*;
import java.time.Instant;

import api.RandomFoxAPI;
import static config.BotConstants.*;

public class FoxCommand {

    public static void handleFoxCommand(SlashCommandInteraction event) {
        String randomFoxURL = RandomFoxAPI.fetchRandomFox();

        if (randomFoxURL == null) {
            event.reply("Error fetching random fox image...")
                    .queue();
        } else {
            EmbedBuilder embed = new EmbedBuilder();

            embed.setAuthor("Brownseal Fluffy", BOT_URL, BOT_IMAGE_URL);
            embed.setTitle("Hello world, this is a fox!");
            embed.setImage(randomFoxURL);
            embed.setColor(new Color(195, 119, 4));
            embed.setFooter("Courtesy of randomfox.ca");
            embed.setTimestamp(Instant.now());
            event.replyEmbeds(embed.build()).queue();
            embed.clear();
        }
    }

}
