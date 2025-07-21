package com.pluralsight;

import java.util.Scanner;

public class Report {
    public  static void display(String message){
        System.out.println(message);
    }
//    Report screen
    public static void ReportScreen(){
        Scanner scanner = new Scanner(System.in);
        display("------- Report Screen -------");
        boolean option = true;
        while (option){
            display("Search By.......");
            display("1) - Month To Date");
            display("2) - Previous Month");
            display("3) - Year to Date");
            display("4) - Previous Year");
            display("5) - Vendor");
            display("6) - Custom Search");
            display("0) - Back to Ledger Screen");
            display("Enter command: ");
            int res = scanner.nextInt();

            switch (res){
                case 1:
                    TransactionManager.searchByCurrentDate();
                    break;
                case 2:
                    TransactionManager.searchByPreviousDate();
                    break;
                case 3:
                    TransactionManager.searchByCurrentYear();
                    break;
                case 4:
                    TransactionManager.searchByPreviousYear();
                    break;
                case 5:
                    TransactionManager.searchByVendor();
                    break;
                case 6:
                    TransactionManager.customSearch();
                    break;
                case 0:
                    display("Returning to Ledger......");
                    option = false;
                    break;

            }
        }
    }
}
