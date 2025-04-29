package com.pluralsight;

import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
   homeScreen();
    }
    public  static void homeScreen(){
        display("Please select Option");
        boolean display = true;
        while (display){
            display("-------- You are in the Home Screen ---------");
            display("1 - Add Deposit");
            display("2 - Make Payment (Debit)");
            display("3 - Ledger Screen");
            display("4 - Exit");
            int res = scanner.nextInt();
            switch (res){
                case 1:
                    TransactionManager.addDeposit();
                    break;
                case 2:
                    TransactionManager.makePayment();
                    break;
                case 3:
                    Ledger.ledgerScreen();
                case 4:
                    display = false;
                    return;
            }
        }
    }
    public static void display(String message){
        System.out.println(message);
    }
}
