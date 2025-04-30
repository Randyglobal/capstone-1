package com.pluralsight;

import java.util.Scanner;

public class Option {
    public  static void display(String message){
        System.out.println(message);
    }
    public static void optionSearch(){
        Scanner scanner = new Scanner(System.in);
        display("------- Report Screen -------");
        boolean option = true;
        while (option){
            display("Search By.......");
            display("1) - Start Date");
            display("2) - End Date");
            display("3) - Description");
            display("4) - Vendor");
            display("5) - Amount");
            display("0) - Back to Report Page");
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
                    display("Other options.....");
                    Option.optionSearch();
                    break;
                case 0:
                    display("Returning to Report......");
                    option = false;
                    break;

            }
        }
    }
}

