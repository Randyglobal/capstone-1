package com.pluralsight;

import java.io.*;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
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
//   Get user info
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
//  display file info method
    public static void displayTransaction(Transaction transaction){
        display("Name: " + transaction.getName() + "\n"
                + "Date: " + transaction.getDate() + "\n" +
                "Time: " + transaction.getTime() + "\n" +
                "Description: " + transaction.getDescription() + "\n" +
                "Vendor: " + transaction.getVendor() + "\n" +
                "Amount: $" + transaction.getAmount());
        display(" ");
    }
//    Read file method
    public static void breakTransactionLine(){
         try {
             BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
             String line;
             while ((line = bufferedReader.readLine()) != null){
                 String[] linePart = line.split("\\|");
                 if (linePart.length == 6) {
                     name = linePart[0];
                     LocalDate date = LocalDate.parse(linePart[1]);
                     time = String.valueOf(LocalTime.parse(linePart[2]));
                     description = linePart[3];
                     vendor = linePart[4];
                     amount = Double.parseDouble(linePart[5]);
                     transactionList.add(new Transaction(name, String.valueOf(date), time, vendor, description, amount));

                 }else {
                     display("Invalid" + line);
                 }
             }
         } catch (IOException e) {
             display("Invoked Error");
         }
}
//display search
    public static void displaySearch(List<Transaction> filteredSearch){
        int filteredTransaction = filteredSearch.size();
        if (filteredTransaction == 0){
            display("Invalid search");
        }else{
            display("--------- Result found: " + filteredTransaction + " -------");
            for (Transaction transaction : filteredSearch){
                displayTransaction(transaction);
            }
        }
    }
//  read all transaction
    public static void readFile()
    {
       displaySearch(transactionList);
    }
//    read all payments
    public static void readPayment(){
//         storing all the payments in a seperate array
       List<Transaction> payment = new ArrayList<>();
       for (Transaction transaction: transactionList){
           if (transaction.getAmount() < 0){
               payment.add(transaction);
           }
       }
       displaySearch(payment);
    }
//   Read all deposits
    public static void readDeposit(){
        List<Transaction> deposit = new ArrayList<>();
        for (Transaction transaction: transactionList){
            if (transaction.getAmount() > 0){
                deposit.add(transaction);
            }
        }
        displaySearch(deposit);
    }
    //    search by current date
    public static void searchByCurrentDate() {
        LocalDate inputMonth = LocalDate.now();
        int currentMonth = inputMonth.getMonthValue();
        display("Result based current month (" + inputMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")) + "):");
        List<Transaction> filteredMonth = new ArrayList<>();
        boolean found = false;
        for (Transaction transaction : transactionList) {
            LocalDate currentMonthTransaction = LocalDate.parse(transaction.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            int transactionMonth = currentMonthTransaction.getMonthValue();
            if (transactionMonth == currentMonth) {
                filteredMonth.add(transaction);
                found = true;
            }
        }
        displaySearch(filteredMonth);
        if (!found) {
            display("No transactions found for vendor: " + currentMonth);
        }
    }
    //    search by previous date
    public static void searchByPreviousDate() {
        LocalDate inputMonth = LocalDate.now();
        LocalDate previousMonth = inputMonth.minusMonths(1);
        int previousMonthValue = previousMonth.getMonthValue();
        display("Result Based on Previous Month (" + previousMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")) + "):");
        List<Transaction> filteredMonth = new ArrayList<>();
        boolean found = false;
        for (Transaction transaction : transactionList) {
            LocalDate currentMonthTransaction = LocalDate.parse(transaction.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            int transactionMonth = currentMonthTransaction.getMonthValue();
            if (transactionMonth == previousMonthValue) {
                filteredMonth.add(transaction);
                found = true;
            }
        }
        displaySearch(filteredMonth);
        if (!found) {
            display("No transactions found for vendor: " + previousMonth);
        }
    }
    //    search by current Year
    public static void searchByCurrentYear() {
        LocalDate inputYear = LocalDate.now();
        int currentYear = inputYear.getYear();
        display("Result based on current Year (" + inputYear.format(DateTimeFormatter.ofPattern("MMMM yyyy")) + "):");
        boolean found = false;
        List<Transaction> filteredYear= new ArrayList<>();
        for (Transaction transaction : transactionList) {
            LocalDate currentMonthTransaction = LocalDate.parse(transaction.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            int transactionYear= currentMonthTransaction.getYear();
            if (transactionYear == currentYear) {
                filteredYear.add(transaction);
                found = true;
            }
        }
       displaySearch(filteredYear);
        if (!found) {
            display("No transactions found for vendor: " + currentYear);
        }
    }
    //    search by previous Year
    public static void searchByPreviousYear() {
        LocalDate inputYear = LocalDate.now();
        LocalDate previousYear = inputYear.minusYears(1);
        int previousYearValue = previousYear.getYear();
        display("Result Based on Previous Year (" + previousYear.format(DateTimeFormatter.ofPattern("MMMM yyyy")) + "):");
        List<Transaction> filteredYear= new ArrayList<>();
        boolean found = false;
        for (Transaction transaction : transactionList) {
            LocalDate currentMonthTransaction = LocalDate.parse(transaction.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            int transactionMonth = currentMonthTransaction.getYear();
            if (transactionMonth == previousYearValue) {
                filteredYear.add(transaction);
                found = true;
            }
        }
        displaySearch(filteredYear);
        if (!found) {
            display("No transactions found for vendor: " + previousYear);
        }
    }
//    Search by vendor
    public static void searchByVendor() {
        display("Enter Vendor Name: ");
        String inputVendor = scanner.nextLine().trim();
        List<Transaction> filteredVendor = new ArrayList<>();
        boolean found = true;
        for (Transaction transaction : transactionList) {
            if (transaction.getVendor().equalsIgnoreCase(inputVendor)) {
                filteredVendor.add(transaction);
                found = true;
            }
        }
        displaySearch(filteredVendor);
        if (!found) {
            display("No transactions found for vendor: " + inputVendor);
        }
    }

//    custom search
    public static void customSearch(){
         display("Enter start date e.g (2025-04-30)");
         String startDateToString = scanner.nextLine();
         LocalDate startDate = LocalDate.parse(startDateToString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
         display("Enter end date e.g (2025-04-30)");
         String endDateToString = scanner.nextLine();
         display("Enter Description");
         String descriptionFilter = scanner.nextLine();
        display("Enter Vendor");
        String vendorFilter = scanner.nextLine();
        display("Enter amount");
        double amountFilter = scanner.nextDouble();
         LocalDate endDate = LocalDate.parse(endDateToString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
         List<Transaction> customSearch = new ArrayList<>();
         boolean res = false;
         for (Transaction transaction : transactionList){
             LocalDate transactionDate = LocalDate.parse(transaction.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
             String transDescription = transaction.getDescription();
             String transVendor = transaction.getVendor();
             int transAmount = (int) transaction.getAmount();
             boolean date = false;
             boolean desMatch = false;
             boolean vendorMatch = false;
             boolean amountMatch = false;
             if (transactionDate.isBefore(endDate) && transactionDate.isAfter(startDate)){
                 date = true;
             }
             if (descriptionFilter.isEmpty() || transDescription.equalsIgnoreCase(descriptionFilter)){
                 desMatch = true;
             }
             if (vendorFilter.isEmpty() || transVendor.equalsIgnoreCase(vendorFilter)){
                 vendorMatch = true;
             }
             if (amountFilter != 0 || transAmount >= 0){
                 amountMatch = true;
             }
             if (date && desMatch && vendorMatch && amountMatch){
                 customSearch.add(transaction);
                 res = true;
             }
         }
         displaySearch(customSearch);
         if (!res){
             display(
                     "Invalid Entry"
             );
         }
    }

}
