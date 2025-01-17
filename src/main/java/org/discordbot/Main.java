package org.discordbot;

import commands.CommandManager;
import database.DatabaseConnection;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Main class for the Discord bot.
 *
 * This class initializes the bot, reads the Discord token from a file, and CAN refresh the guild commands if edited.
 *
 * @author Vettel53
 */
public class Main {

    /**
     * Main method to run the bot.
     *
     * @param args Command-line arguments (not used in this case).
     * @throws InterruptedException If the bot initialization is interrupted.
     */
    public static void main(String[] args) throws InterruptedException {
        String data = null;

        try {
            File myToken = new File("src/main/java/config/discordToken.txt");
            Scanner myReader = new Scanner(myToken);
            while (myReader.hasNextLine()) {
                data = myReader.nextLine();
                System.out.println("Your token is: " + data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred while reading the file...");
            e.printStackTrace();
            System.exit(1);
        }

        // Load Database and create tables
        DatabaseConnection.loadDatabaseProperties();

        // Initialize commandManager class
        CommandManager manager = new CommandManager();

        // Build JDA instance after valid token is processed.
        JDA bot =  JDABuilder.createDefault(data)
                .setActivity(Activity.streaming("Dying of Thirst", "https://www.youtube.com/watch?v=9-XbXBq8cl8"))
                .addEventListeners(manager)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .build().awaitReady();

        //refreshCommands(bot);
        manager.setBot(bot);
        manager.addCommands();

    }

    /**
     * Refreshes the guild commands.
     *
     * Deletes all guild commands and prints the names of the deleted commands.
     *
     * @param bot The JDA instance representing the bot.
     */
    public static void refreshCommands(JDA bot) {
        Guild guild;
        // This guild is my personal testing server, eventually global commands will be rolled out
        guild = bot.getGuildById(1293823251004264448L);

        // Delete all guild commands
        // Notes: retrieveCommands gets stored in commandsToDelete and gets passed to next block of code and so on
        // commandsToDelete -> means its getting passed as a parameter to next block of code
        if (guild != null) {
            guild.retrieveCommands().queue(commandsToDelete -> {
                commandsToDelete.forEach(command -> {
                    command.delete().queue(success -> {
                        System.out.println("Deleted guild command: " + command.getName());
                    });
                });
            });
        }
    }

}