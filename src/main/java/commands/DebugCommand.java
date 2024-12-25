package commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.awt.*;


// Debugging command, edit to random stuff when testing
public class DebugCommand {

    public static void handleDebugCommand(SlashCommandInteraction event, EmbedBuilder embed) {
        embed.setTitle("Debug Command");
        embed.setDescription("This is a debug command.");
        embed.setColor(new Color(255,255,255));
        embed.setFooter("Debugging");
        event.replyEmbeds(embed.build()).queue();
    }

}
