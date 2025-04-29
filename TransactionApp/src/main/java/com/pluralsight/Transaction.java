package com.pluralsight;

import java.time.LocalTime;

public class Transaction {
    private String name = "";
    private String date = "";
    private String time;
    private String vendor = "";
    private String description = "";
    private double amount = 0;

    public Transaction(String name, String date, String time, String vendor, String description, double amount) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.vendor = vendor;
        this.description = description;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
