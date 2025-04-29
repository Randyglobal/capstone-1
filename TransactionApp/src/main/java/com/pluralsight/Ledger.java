package com.pluralsight;

import java.util.Scanner;

public class Ledger {
    public  static void display(String message){
        System.out.println(message);
    }
    public static void ledgerScreen(){
        Scanner scanner = new Scanner(System.in);
     display("-------  Ledger Screen -------");
     boolean option = true;
     while (option){
         display("A) - View all entries");
         display("B) - View deposit only");
         display("C) - View payments only");
         display("D) - Reports");
         display("Enter command: ");
         String res = scanner.nextLine();

         switch (res){
             case "A":
                 TransactionManager.readFile();
             case "B":
                TransactionManager.readDeposit();
             case "C":
                 TransactionManager.readPayment();
         }
     }
    }

}
