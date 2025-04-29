package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TransactionApp {
    static Scanner scanner = new Scanner(System.in);
    static String fileName = "transactions.csv";
    static String date;
    static LocalTime time;
    static String transactionVendor;
    static String transactionDescription;
    static double amount;


//   display result
    public static void display(String message){
        System.out.println(message);
    }
    public static void main(String[] args) {
      display("Please Select from the Option Below");
      boolean display = true;
      while (display){
          display(" 1 - Transactions");
          display(" 2 - View all books");
          int res = scanner.nextInt();
          switch (res){
              case 1:
                  addTransaction();
                  break;
              case 2:
                  viewAllTransactions();
                  break;
              case 3:
                  display = false;
                  return;
          }
      }
    }

    public static void addTransaction(){
        display("Hello Welcome to transaction App!!!");
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateInput = null;
        String transactionDate = "";
        boolean isValid = false;

        while (!isValid){
            display("Enter Date (e.g 2025-06-12): ");
            date = scanner.nextLine();

            try {
                dateInput = LocalDate.parse(date, fmt);
                transactionDate = fmt.format(dateInput);
                isValid = true;
                display("Entered date: " + fmt.format(dateInput));
            } catch (DateTimeParseException e) {
                display("Invalid Entry");
            }

        }
        display("Enter Description: ");
        transactionDescription = scanner.nextLine();
        display("Enter Vendor: ");
        transactionVendor = scanner.nextLine();
        display("Enter Amount: ");
        amount = scanner.nextDouble();

        time = LocalTime.now();
        String localTime = time.toString();
        saveTransaction(transactionDate, localTime, transactionDescription, transactionVendor, amount);

    }
    public static void saveTransaction(String date, String time, String description, String vendor, double amount){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
            String line = String.join("|", date, time, description, vendor, String.valueOf(amount));
            writer.write(line);
            writer.newLine();
            display("Transaction Saved");
            writer.close();
        } catch (Exception e) {
            display("Error Handler Activated");
        }
    }
    public static List<String[]> readTransaction(){
        List<String[]> transactions = new ArrayList<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = bufferedReader.readLine()) != null){
                String[] lineParts = line.split("\\|");
                if (lineParts.length == 5){
                    transactions.add(lineParts);
                }else{
                    display("Error invoked");
                }
            }
        } catch (IOException e) {
            display("Error");
        }
        return transactions;
    }
    public static void viewAllTransactions(){
        List<String[]> allTransactions = readTransaction();
        display("------------ All Transactions -----------");
        for (String[] transactions : allTransactions){
            display("Date: " + transactions[0] + "\n"
                     + "Time: " + transactions[1] + "\n"
                      + "Description: " + transactions[2] + "\n"
                        + "Vendor: " + transactions[3] + "\n"
                          + "Amount: " + transactions[4]);
            display("");

        }
    }
}
