package com.example.gelda.wallet.controller;

import com.example.gelda.wallet.service.WalletService;
import com.example.gelda.wallet.entity.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {

    private final WalletService walletService;

    @Autowired
    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PreAuthorize("USER")
    @GetMapping("/balance/{userId}")
    public double getBalance(@PathVariable Long userId) {
        return walletService.getBalance(userId);
    }
}
