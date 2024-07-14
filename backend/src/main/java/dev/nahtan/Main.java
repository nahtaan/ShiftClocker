package dev.nahtan;

import org.tinylog.Logger;

public class Main {

    public static void main(String... args) {
        Logger.info("Starting Backend Server");

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
}