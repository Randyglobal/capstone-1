package com.pluralsight;

import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static int res;
    public static void main(String[] args) {
        TransactionManager.readFile();
       res = homeScreen();
    }
    public  static int homeScreen(){
        display("Please select Option");
        boolean display = true;
        while (display){
            display("-------- You are in the Home Screen ---------");
            display("1 - Add Deposit");
            display("2 - Make Payment (Debit)");
            display("3 - Ledger Screen");
            display("4 - Exit");
            display("Enter Command: ");
            res = scanner.nextInt();
            scanner.nextLine();
            switch (res){
                case 1:
                    TransactionManager.addDeposit();
                    break;
                case 2:
                    TransactionManager.makePayment();
                    break;
                case 3:
                    Ledger.ledgerScreen();
                    break;
                case 4:
                    display("Exiting app.... Bye...... \uD83D\uDE0A");
                    display = false;
                    break;
            }
        }
        return res;
    }
    public static void display(String message){
        System.out.println(message);
    }
}
