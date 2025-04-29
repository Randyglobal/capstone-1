package com.pluralsight;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class TransactionManager {
//    initializing
    private static String fileName = "transaction.csv";
    private String date = "";
    private String time = "";
    private String description = "";
    private String vendor = "";
    private double abmout = 0;


    public static void saveTransaction(String date, String time, String description, String vendor, double amount){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
            String line = String.join("|", date, time, description, vendor, String.valueOf(amount));
            writer.write(line);
            writer.newLine();
            System.out.println("Transaction Saved");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
