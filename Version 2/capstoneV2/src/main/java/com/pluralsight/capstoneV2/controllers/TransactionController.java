package com.pluralsight.capstoneV2.controllers;

import com.pluralsight.capstoneV2.data.TransactionDao;
import com.pluralsight.capstoneV2.models.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController

@RequestMapping("transactions")

@CrossOrigin
public class TransactionController {
    private TransactionDao transactionDao;

    @Autowired
    public TransactionController(TransactionDao transactionDao) {
        this.transactionDao = transactionDao;
    }

    @GetMapping("")
    public List<Transaction> getAll(){
        try {
            return transactionDao.getAllTransaction();
        }catch (Exception e){
            System.err.println("Error fetching all transactions" + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad. Could not retrieve transactions.");
        }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Transaction addTransaction(@RequestBody Transaction transaction){
        try {
            return transactionDao.create(transaction);
        } catch (Exception e) {
            System.err.println("Error creating transactions" + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad. Could not create transaction.");
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> updateTransaction(@PathVariable int id, @RequestBody Transaction transaction)
    {
        // update the category by id
        try {
            // Ensuring ID matches
            if (transaction.getTransaction_id() == 0){
                transaction.setTransaction_id(id);
            }else if(transaction.getTransaction_id() != id){
                throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transaction ID in path does not match ID in request body.");
            }
            Transaction existingTransaction = transactionDao.getById(id);
//            error exception
            if (existingTransaction == null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "transaction to be updated not found");
            }
            transactionDao.updateTransaction(id, transaction);
            return ResponseEntity.ok().build();
        }catch (ResponseStatusException e){
            throw new RuntimeException(e);
        }
        catch (Exception e){
            System.err.println("Error updating transaction: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad. Could not retrieve transaction.");
        }
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTransaction(@PathVariable int id)
    {
        // delete the category by id
        try {
            Transaction existingTransaction = transactionDao.getById(id);
//            error exception
            if (existingTransaction == null){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction to be deleted not found");
            }
            transactionDao.delete(id);
        }catch (Exception e){
            System.err.println("Error deleting Transaction: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad. Could not retrieve transaction.");
        }
    }
}
