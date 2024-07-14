package dev.nahtan;

import org.tinylog.Logger;

import java.io.File;
import java.net.URISyntaxException;

public class Main {
    private static Config config;

    public static void main(String... args) {
        Logger.info("Starting Backend Server");
        config = new Config();

        // Stop execution if the config cannot be loaded. The Config class will handle sending logs to the console.
        if(!config.loadDefaultFile()) {
            return;
        }




        /*
            DATA LAYOUT
            - Users each need an email, username, password and an internal ID
            - Each internal ID is unique and can be used to find the shifts of the user
            - Shifts each need a shift id, user id, start date, end date, duration, ?rate, location name, payment received
                (rate can be nullable as some jobs may pay by the shift rather than by hour)
                (although duration could be calculated from the start and end date, it's simpler to just store it pre-calculated for easier use later)

            OTHER USER DATA
            - Total Earned
            - Additional Incomes e.g. Tips that are calculated separately
            - List of stored locations
            - Total Time Worked
            - Avg. hourly rate including other incomes
         */



    }

    /**
     * @return The path of the folder containing the Jar File.
     */
    public static String getContainerPath() {
        try{
            return new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile().getPath();
        }catch (URISyntaxException e){
            return null;
        }
    }

    /**
     * @return The path of the Jar file, including the Jar file itself.
     */
    public static String getJarPath() {
        try {
            return new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath();
        }catch (URISyntaxException e) {
            return null;
        }
    }

}