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
//
//    // Deposit money into the user's wallet
//    public Wallet deposit(User user, double amount) {
//        Wallet wallet = walletRepository.findByUserId(user.getId());
//        if (wallet == null) {
//            throw new RuntimeException("Wallet not found for this user.");
//        }
//
//        wallet.setBalance(wallet.getBalance() + amount);  // Add money to wallet balance
//        walletRepository.save(wallet);  // Save updated wallet
//
//        // Record the deposit transaction
//        transactionService.deposit(user.getId(), new TransactionAmountDTO(amount));  // Assuming you have a DTO for amount
//        return wallet;
//    }
//
//    // Withdraw money from the user's wallet
//    public Wallet withdraw(User user, double amount) {
//        Wallet wallet = walletRepository.findByUserId(user.getId());
//        if (wallet == null) {
//            throw new RuntimeException("Wallet not found for this user.");
//        }
//
//        if (wallet.getBalance() < amount) {
//            throw new RuntimeException("Insufficient funds.");
//        }
//
//        wallet.setBalance(wallet.getBalance() - amount);  // Subtract money from wallet balance
//        walletRepository.save(wallet);  // Save updated wallet
//
//        // Record the withdrawal transaction
//        transactionService.withdraw(user.getId(), new TransactionAmountDTO(amount));  // Assuming you have a DTO for amount
//        return wallet;
//    }
//
//    // Transfer money from one user's wallet to another user's wallet
//    public void transfer(User sender, User receiver, double amount) {
//        if (sender.equals(receiver)) {
//            throw new RuntimeException("Cannot transfer money to yourself.");
//        }
//
//        // Withdraw money from the sender's wallet
//        withdraw(sender, amount);
//
//        // Deposit money into the receiver's wallet
//        deposit(receiver, amount);
//
//        // Record the transfer transaction
//        transactionService.transfer(sender.getId(), new TransferTransactionDTO(receiver.getMobileNumber(), amount));
//    }
}
