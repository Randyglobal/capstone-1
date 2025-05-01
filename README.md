# capstone-1
capstone1

# Transaction Management System

## Overview

This project is a fundamental transaction management system developed as a capstone project to demonstrate proficiency in Java programming concepts acquired over the past three weeks. The application provides users with the ability to record financial transactions, view a comprehensive list of these transactions, and navigate between different sections or "screens" within the application to access various functionalities. This project emphasizes core Java principles and basic application structure.

## Features

* **Add Transaction:** This feature allows users to input the essential details of a new financial transaction. Typically, this includes:
    * **Description:** A brief explanation of the transaction (e.g., "Grocery Shopping," "Salary Deposit").
    * **Amount:** The monetary value of the transaction.
    * **Date:** The date on which the transaction occurred.
    * **Vendor:** The seller
* **View Transactions:** This feature presents a clear and organized display of all the transactions that have been added to the system. This view likely shows the details of each transaction (description, amount, date, etc.).
* **Navigation:** The application is designed with the capability to navigate between different "screens" or sections. This suggests a modular structure where different functionalities are organized into distinct parts of the application. This could involve moving between the "Add Transaction" screen, the "View Transactions" screen, and potentially others.

## Technologies Used

* **Java:** The primary programming language used for the development of this transaction management system.

## Getting Started

1.  **Prerequisites:** Ensure that you have the **Java Development Kit (JDK)** installed on your computer. You can download the latest version from the official Oracle website or through other open-source distributions like OpenJDK.

2.  **Compilation:**
    * Open your terminal or command prompt.
    * Navigate to the root directory of your project (the folder containing the `com` directory).
    * Compile all the Java source files within the `com.pluralsight` package using the following command:
        ```bash
        javac com/pluralsight/*.java
        ```
    * This command will compile all the `.java` files in the specified package and generate `.class` files in the same directory structure.

3.  **Execution:**
    * In the same terminal or command prompt, execute the main application class using the Java Virtual Machine:
        ```bash
        java com.pluralsight.main
        ```
    * This command will launch your transaction management system by running the `main` method located in the `Main.java` file within the `com.pluralsight` package.

## Usage

1.  **Initial Interface:** Upon running the application, you will likely encounter a text-based menu or a simple graphical user interface (depending on how you implemented the "screens"). This interface will present you with options for the available features.

2.  **Adding a New Transaction:**
    * Select the option corresponding to "Add Transaction."
    * The application will prompt you to enter the details of the transaction, such as the description, amount, and date. Follow the on-screen instructions to input this information.
    * Once you have entered all the required details, the transaction will be recorded in the system.

3.  **Viewing Transactions:**
    * Select the option corresponding to "View Transactions."
    * The application will display a list of all the transactions that have been added. This display might show each transaction's description, amount, and date.

4.  **Navigating Between Screens:**
    * The application will provide options or controls to move between different parts of the application (the "screens"). For example, there might be a menu option to go back to the main menu from the "View Transactions" screen, or a button to navigate from the main menu to the "Add Transaction" screen. Follow the prompts or use the provided controls to navigate.

## Project Structure

The project files are organized within the following directory structure:
TransactionSystem/

├── com/pluralsight/

│   ├── Transaction.java         // Defines the structure and properties of a Transaction object. This class likely contains the constructor and methods to access transaction details.

│   ├── TransactionManager.java  // Manages a collection of Transaction objects and implements the core operations of the application, such as adding new transactions and retrieving the list of transactions.

│   ├── Report.java             // Represents one of the "screens" or user interface components.

│   ├── Ledger.java             // Represents another "screen" or user interface component

│   └── Main.java                // The main entry point of the application. This class contains the main method that starts the program execution and likely orchestrates the initial user interface and navigation.

└── README.md                    // This file, providing information about the project.