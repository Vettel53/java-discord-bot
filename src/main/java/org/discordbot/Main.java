package org.discordbot;

import events.BotListeners;
import events.Commands;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
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
                .setActivity(Activity.playing("Rocket League"))
                .addEventListeners(new BotListeners())
                .addEventListeners(commands)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .build().awaitReady();

        commands.setBot(bot);

    }
}