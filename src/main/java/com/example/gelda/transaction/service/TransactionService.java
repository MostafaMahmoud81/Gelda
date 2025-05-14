package com.example.gelda.transaction.service;

import com.example.gelda.transaction.dto.TransferTransactionDTO;
import com.example.gelda.transaction.dto.TransactionAmountDTO;
import com.example.gelda.transaction.entity.Transaction;
import com.example.gelda.transaction.repository.TransactionRepository;
import com.example.gelda.user.entity.User;
import com.example.gelda.wallet.service.WalletService;
import com.example.gelda.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import com.example.gelda.transaction.dto.TransactionHistoryDTO;


@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final WalletService walletService;  // Inject WalletService

    @Autowired
    public TransactionService(TransactionRepository transactionRepository,
                              UserRepository userRepository,
                              WalletService walletService) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.walletService = walletService;
    }

    // Deposit method - Updates balance and creates a transaction record
    public void deposit(Long userId, TransactionAmountDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Get the current balance
        double currentBalance = walletService.getBalance(userId);
        double newBalance = currentBalance + dto.getAmount();

        // Update the wallet balance using WalletService
        walletService.updateBalance(userId, newBalance);

        // Record the deposit transaction
        Transaction transaction = new Transaction("Deposit", dto.getAmount(), user, null);
        transactionRepository.save(transaction);
    }

    // Withdraw method - Updates balance and creates a transaction record
    public void  withdraw(Long userId, TransactionAmountDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Get the current balance
        double currentBalance = walletService.getBalance(userId);
        if (currentBalance < dto.getAmount()) {
            throw new RuntimeException("Insufficient funds.");
        }

        double newBalance = currentBalance - dto.getAmount();

        // Update the wallet balance using WalletService
        walletService.updateBalance(userId, newBalance);

        // Record the withdrawal transaction
        Transaction transaction = new Transaction("Withdraw", dto.getAmount(), user, null);
        transactionRepository.save(transaction);
    }

    // Transfer method - Handles the transfer between users, updates both balances and creates a transaction record
    public void  transfer(Long senderId, TransferTransactionDTO dto) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        User receiver = userRepository.findByMobileNumber(dto.getReceiverMobileNumber())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        if (sender.equals(receiver)) {
            throw new RuntimeException("Cannot transfer money to yourself.");
        }

        // Get sender's current balance
        double senderBalance = walletService.getBalance(senderId);
        if (senderBalance < dto.getAmount()) {
            throw new RuntimeException("Sender has insufficient funds.");
        }

        // Get receiver's current balance
        double receiverBalance = walletService.getBalance(receiver.getId());

        // Update both sender and receiver balances
        walletService.updateBalance(senderId, senderBalance - dto.getAmount());
        walletService.updateBalance(receiver.getId(), receiverBalance + dto.getAmount());

        Transaction transaction = new Transaction("Transfer", dto.getAmount(), sender, receiver);
        transactionRepository.save(transaction); // still saved, just not returned
    }

    public List<TransactionHistoryDTO> getTransactionsByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Transaction> transactions = transactionRepository.findBySenderOrReceiver(user, user);

        return transactions.stream()
                .map(tx -> new TransactionHistoryDTO(
                        tx.getType(),
                        tx.getAmount(),
                        tx.getSender() != null ? tx.getSender().getName() : null,
                        tx.getReceiver() != null ? tx.getReceiver().getName() : null,
                        tx.getTransactionDate()
                ))
                .collect(Collectors.toList());
    }



}
