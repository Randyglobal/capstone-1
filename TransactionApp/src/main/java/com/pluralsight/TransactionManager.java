package com.pluralsight;

import java.io.*;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class TransactionManager {
      private static ArrayList<Transaction> transactionList = new ArrayList<>();
      static Scanner scanner = new Scanner(System.in);
        static String name;
        static LocalDate date;
        static String time;
        static String vendor;
        static String description;
        static double amount;
        static String fileName = "transactions.csv";


    //        output display
public static void display(String message){
    System.out.println(message);
}
public static void addTransactions() {
    display("-------- Hello Welcome to TransactionApp ---------");
    display("Enter your name: ");
    name = scanner.nextLine().trim();
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate dateInput = null;
    String transactionDate = "";
    boolean isValid = false;

    while (!isValid){
        display("Enter Date (e.g 2025-04-29): ");
        date = LocalDate.parse(scanner.nextLine());
        try {
            dateInput = LocalDate.parse(date, fmt);
            transactionDate = fmt.format(dateInput);
            isValid = true;
            display("Entered date: " + dateInput);
        } catch (DateTimeParseException e) {
            display("Invoked Error" + e);
        }
    }
    display("Enter description: ");
    description = scanner.nextLine();
    display("Enter Vendor: ");
    vendor = scanner.nextLine();
    display("Enter Amount: ");
    amount = scanner.nextDouble();
    time = String.valueOf(LocalTime.now());
    LocalTime inputTime = LocalTime.parse(time);
    Transaction transaction = new Transaction(name, transactionDate, inputTime, vendor, description, amount);

    transactionList.add(transaction);
    writeToFile(transaction);
}
public static void writeToFile(Transaction transaction){
 try {
     BufferedWriter Writer = new BufferedWriter(new FileWriter(fileName, true));
     String line = String.join("|", transaction.getName(), transaction.getDate(), transaction.getTime(), transaction.getDescription(), transaction.getVendor(),String.valueOf(transaction.getAmount()));
     Writer.write(line);
     Writer.newLine();
     display("Transaction Saved");
     Writer.close();
 } catch (Exception e) {
     display("Invoked Error");
 }
}
public static void readFile(){
    try {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = bufferedReader.readLine()) != null){
            String[] linePart = line.split("\\|");
             name = linePart[0];
             LocalDate date = LocalDate.parse(linePart[1]);
             time = LocalTime.parse(linePart[2]);
            description = linePart[3];
            vendor = linePart[4];
            amount = Double.parseDouble(linePart[5]);

            display("Name: " + name + "\n"
            + "Date: " + date + "\n" +
                    "Time: " + time + "\n" +
                    "Description: " + description + "\n" +
                    "Vendor: " + vendor + "\n" +
                    "Amount: " + amount);
            display(" ");
        }
    } catch (IOException e) {
        display("Invoked Error" + e);
    }
}


}
