package com.pluralsight;

import java.util.Scanner;

public class Ledger {
    public  static void display(String message){
        System.out.println(message);
    }
//    Ledger Screen
    public static void ledgerScreen(){
        Scanner scanner = new Scanner(System.in);
     display("-------  Ledger Screen -------");
     boolean option = true;
     while (option){
         display("A) - View all entries");
         display("D) - View deposit only");
         display("P) - View payments only");
         display("R) - Reports");
         display("H) - Return to Home Screen");
         display("Enter command: ");
         String res = scanner.nextLine();

         switch (res){
             case "A":
                 TransactionManager.readFile();
                 break;
             case "D":
                TransactionManager.readDeposit();
                break;
             case "P":
                 TransactionManager.readPayment();
                 break;
             case "R":
                 display("Routing to Report Screen....");
                 Report.ReportScreen();
                 break;
             case "H":
                 display("Returning to Home Screen......");
                 option = false;
                 break;
         }
     }
    }

}
