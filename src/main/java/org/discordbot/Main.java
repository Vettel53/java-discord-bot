package org.discordbot;

import events.BotListeners;
import events.Commands;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws InterruptedException {
        String data = null;

        try {
            File myToken = new File("src/token.txt");
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

        // Initially ran into an issue where I wanted to pass the bot object to other classes like Commands.java to
        // help with modularization.
        // Unfortunately, I was having issues doing it, so I came up with this idea.
            // 1. First, we initialize the Command class empty constructor.
            // 2. I use a setter method after the bot is initialized to pass it to the Command class.
        // STEPS: Command class initialized - > bot built - > setter method passes bot to Command class

        Commands commands = new Commands();

        // Build JDA instance after valid token is processed.
        JDA bot =  JDABuilder.createDefault(data)
                .setActivity(Activity.streaming("Dying of Thirst", "https://www.youtube.com/watch?v=9-XbXBq8cl8"))
                .addEventListeners(new BotListeners())
                .addEventListeners(commands)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .build().awaitReady();

        //refreshCommands(bot);
        commands.setBot(bot);
        commands.addCommands();

    }

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