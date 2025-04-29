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
        static String date;
        static String time;
        static String vendor;
        static String description;
        static double amount;
        static String fileName = "transactions.csv";


    //        output display
public static void display(String message){
    System.out.println(message);
}

public static Transaction getTransactionDetails(String transactionType) {
    display("-------- Hello Welcome to TransactionApp ---------");
    display("Enter your name: ");
    name = scanner.nextLine().trim();
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate dateInput = null;
    String transactionDate = "";
    boolean isValid = false;

    while (!isValid){
        display("Enter Date (e.g 2025-04-29): ");
        date = scanner.nextLine();
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
    if (transactionType.equalsIgnoreCase("Deposit")){
        display("Enter Amount: $");
        amount = scanner.nextDouble();
    } else if (transactionType.equalsIgnoreCase("Payment")) {
        display("Enter Amount: $");
        amount = -scanner.nextDouble();
    }
    scanner.nextLine();
    LocalTime now = LocalTime.now();
    time = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    return new Transaction(name, transactionDate, time, vendor, description, amount);
}

public static void addDeposit(){
    Transaction transaction = getTransactionDetails("Deposit");
    transactionList.add(transaction);
    writeToFile(transaction);
}
public static void makePayment(){
        Transaction transaction = getTransactionDetails("Payment");
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
             time = String.valueOf(LocalTime.parse(linePart[2]));
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
