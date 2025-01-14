package commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.awt.*;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;

import static config.BotConstants.BOT_IMAGE_URL;
import static config.BotConstants.BOT_URL;

public class AvatarCommand {

        /**
         * <p>
         *     Fetches <b>avatarURL</b> using discord event.
         * </p>
         * <p>
         *     Embeds message and sends it.
         * </p>
         * @param event the SlashCommandInteraction event that triggered this command,
         * providing context and methods to reply to the interaction.
         */
    public static void handleAvatarCommand(SlashCommandInteraction event) {

        /* NOTES: This is the more traditional async, it is the same as the one below
        CompletableFuture.runAsync(new Runnable() {
            @Override
            public void run() {
            // Your asynchronous code here
                }
         });
         */

        //CompletableFuture.runAsync(() -> {
            String avatarURL = event.getUser().getAvatarUrl();
            String userName = event.getUser().getName();

            EmbedBuilder embed = new EmbedBuilder();

            embed.setAuthor("Brownseal", BOT_URL, BOT_IMAGE_URL);
            embed.setTitle("Hello world, this is " + userName + "'s avatar");
            embed.setImage(avatarURL);
            embed.setColor(new Color(220, 73, 15));
            embed.setFooter(userName);
            embed.setTimestamp(Instant.now());
            event.replyEmbeds(embed.build()).queue();
            embed.clear();
        //});
    }

}
