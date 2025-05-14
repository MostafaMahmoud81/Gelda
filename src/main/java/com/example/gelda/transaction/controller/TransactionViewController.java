package com.example.gelda.transaction.controller;

import com.example.gelda.transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/transactions")
public class TransactionViewController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionViewController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }


    // Show deposit form
    @GetMapping("/deposit")
    public String showDepositForm() {
        return "add-transaction"; // Thymeleaf will render "add-transaction.html"
    }

    // Show withdraw form
    @GetMapping("/withdraw")
    public String showWithdrawForm() {
        return "withdraw-transaction"; // Thymeleaf will render "withdraw-transaction.html"
    }

    // Show transfer form
    @GetMapping("/transfer")
    public String showTransferForm() {
        return "transfer-transaction"; // Thymeleaf will render "transfer-transaction.html"
    }
}
