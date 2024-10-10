package org.example;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
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

        JDA bot =  JDABuilder.createDefault(data)
                .setActivity(Activity.playing("Rocket League"))
                .build();
    }
}