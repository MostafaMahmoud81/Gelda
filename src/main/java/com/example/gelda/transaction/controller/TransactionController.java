package com.example.gelda.transaction.controller;

import com.example.gelda.transaction.dto.TransferTransactionDTO;
import com.example.gelda.transaction.dto.TransactionAmountDTO;
import com.example.gelda.transaction.entity.Transaction;
import com.example.gelda.transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // Deposit money into wallet
    @PostMapping("/{userId}/deposit")
    public ResponseEntity<Transaction> deposit(@PathVariable Long userId, @RequestBody TransactionAmountDTO dto) {
        Transaction transaction = transactionService.deposit(userId, dto);
        return ResponseEntity.ok(transaction);
    }

    // Withdraw money from wallet
    @PostMapping("/{userId}/withdraw")
    public ResponseEntity<Transaction> withdraw(@PathVariable Long userId, @RequestBody TransactionAmountDTO dto) {
        Transaction transaction = transactionService.withdraw(userId, dto);
        return ResponseEntity.ok(transaction);
    }

    // Transfer money from one user to another
    @PostMapping("/{userId}/transfer")
    public ResponseEntity<Transaction> transfer(@PathVariable Long userId, @RequestBody TransferTransactionDTO dto) {
        Transaction transaction = transactionService.transfer(userId, dto);
        return ResponseEntity.ok(transaction);
    }
}
