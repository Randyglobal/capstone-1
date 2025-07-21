package com.pluralsight.capstoneV2.data;

import com.pluralsight.capstoneV2.models.Transaction;

import java.util.List;

public interface TransactionDao {
    List<Transaction> getAllTransaction();
    Transaction getById(int categoryId);
    Transaction create(Transaction transaction);
    void updateTransaction(int transactionId, Transaction transaction);
    void delete(int transactionId);
}
