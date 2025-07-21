package com.pluralsight.capstoneV2.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {
    private int transaction_id;
    private String name;
    private LocalDateTime date;
    private String vendor;
    private String description;
    private BigDecimal amount;

    public Transaction(int transaction_id, String name, LocalDateTime date, String vendor, String description, BigDecimal amount) {
        this.transaction_id = transaction_id;
        this.name = name;
        this.date = date;
        this.vendor = vendor;
        this.description = description;
        this.amount = amount;
    }

    public int getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getVendor() {
        return vendor;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transaction_id=" + transaction_id +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", vendor='" + vendor + '\'' +
                ", description='" + description + '\'' +
                ", amount=" + amount +
                '}';
    }
}
