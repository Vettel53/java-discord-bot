package commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.awt.*;


// Debugging command, edit to random stuff when testing
public class DebugCommand {

    /**
     * <p>
     *     Simple debug command where you can edit/test random things.
     * </p>
     * @param event the SlashCommandInteraction event that triggered this command,
     *  providing context and methods to reply to the interaction.
     */
    public static void handleDebugCommand(SlashCommandInteraction event) {
        EmbedBuilder embed = new EmbedBuilder();

        embed.setTitle("Debug Command");
        embed.setDescription("This is a debug command.");
        embed.setColor(new Color(255,255,255));
        embed.setFooter("Debugging");
        event.replyEmbeds(embed.build()).queue();
    }

}
