package com.pluralsight;

import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
    display("Please select Option");
    boolean display = true;
    while (display){
        display("1 - Add Transactions");
        display("2 - View All Transactions");
        display("3 - Exit");
        int res = scanner.nextInt();
        switch (res){
            case 1:
                TransactionManager.addTransactions();
                break;
            case 2:
                TransactionManager.readFile();
                break;
            case 3:
                display = false;
                return;
        }
    }
    }
    public static void display(String message){
        System.out.println(message);
    }
}
