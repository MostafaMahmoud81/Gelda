package com.example.gelda.transaction.controller;

import com.example.gelda.redis.annotations.RateLimit;
import com.example.gelda.transaction.dto.TransferTransactionDTO;
import com.example.gelda.transaction.dto.TransactionAmountDTO;
import com.example.gelda.transaction.entity.Transaction;
import com.example.gelda.transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

@CrossOrigin
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @RateLimit(limit = 1, duration = 10, timeUnit = TimeUnit.SECONDS, keyPrefix = "deposit")
    @PostMapping("/{userId}/deposit" )
    public ResponseEntity<String> deposit(@PathVariable Long userId, @RequestBody TransactionAmountDTO dto) {
        transactionService.deposit(userId, dto);
        return ResponseEntity.ok("Deposit completed successfully.");
    }

    @RateLimit(limit = 1, duration = 10, timeUnit = TimeUnit.SECONDS, keyPrefix = "withdraw")
    @PostMapping("/{userId}/withdraw")
    public ResponseEntity<String> withdraw(@PathVariable Long userId, @RequestBody TransactionAmountDTO dto) {
         transactionService.withdraw(userId, dto);
        return ResponseEntity.ok("Withdraw completed successfully.");
    }

    @RateLimit(limit = 1, duration = 10, timeUnit = TimeUnit.SECONDS, keyPrefix = "transfer")
    @PostMapping("/{userId}/transfer")
    public ResponseEntity<String> transfer(@PathVariable Long userId, @RequestBody TransferTransactionDTO dto) {
        transactionService.transfer(userId, dto);
        return ResponseEntity.ok("Transfer completed successfully.");
    }

}
