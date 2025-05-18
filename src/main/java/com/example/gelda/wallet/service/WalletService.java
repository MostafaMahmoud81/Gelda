package com.example.gelda.wallet.service;

import com.example.gelda.wallet.entity.Wallet;
import com.example.gelda.wallet.repository.WalletRepository;
import com.example.gelda.user.repository.UserRepository;
import com.example.gelda.user.entity.User;
import com.example.gelda.transaction.entity.Transaction;
import com.example.gelda.transaction.dto.TransactionAmountDTO;
import com.example.gelda.transaction.dto.TransferTransactionDTO;
import com.example.gelda.transaction.service.TransactionService;  // Import the TransactionService
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    //private final TransactionService transactionService;  // Inject TransactionService

    @Autowired
    public WalletService(WalletRepository walletRepository,
                         UserRepository userRepository) {
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
        //this.transactionService = transactionService;  // Initialize TransactionService
    }

    // Automatically create a wallet for the user
    public Wallet createWallet(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Wallet wallet = new Wallet(user);
        return walletRepository.save(wallet); // Save the wallet tied to the user
    }

    // Get balance for a user
    public double getBalance(Long userId) {
        Wallet wallet = walletRepository.findByUserId(userId);
        if (wallet == null) {
            throw new RuntimeException("Wallet not found for this user.");
        }
        return wallet.getBalance();
    }

    // Method to update the wallet balance (for deposit and withdraw)
    public void updateBalance(Long userId, double newBalance) {
        Wallet wallet = walletRepository.findByUserId(userId);
        if (wallet == null) {
            throw new RuntimeException("Wallet not found for this user.");
        }

        wallet.setBalance(newBalance); // Update balance
        walletRepository.save(wallet); // Persist the updated wallet
    }
}
