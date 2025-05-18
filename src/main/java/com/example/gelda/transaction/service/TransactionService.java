package com.example.gelda.transaction.service;

import com.example.gelda.transaction.dto.TransferTransactionDTO;
import com.example.gelda.transaction.dto.TransactionAmountDTO;
import com.example.gelda.transaction.dto.TransactionHistoryDTO;
import com.example.gelda.transaction.entity.Transaction;
import com.example.gelda.transaction.repository.TransactionRepository;
import com.example.gelda.user.entity.User;
import com.example.gelda.user.repository.UserRepository;
import com.example.gelda.wallet.service.WalletService;
import com.example.gelda.redis.service.Redis;  // Updated import
import com.example.gelda.redis.annotations.DistributedLock;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private static final String TRANSACTIONS_CACHE_KEY_PREFIX = "transactions:user:";
    private static final Duration CACHE_TTL = Duration.ofMinutes(5);

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final WalletService walletService;
    private final Redis redis;  // Updated field
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public TransactionService(TransactionRepository transactionRepository,
                              UserRepository userRepository,
                              WalletService walletService,
                              Redis redis) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.walletService = walletService;
        this.redis = redis;
    }

    // Deposit method
    public void deposit(Long userId, TransactionAmountDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        double currentBalance = walletService.getBalance(userId);
        double newBalance = currentBalance + dto.getAmount();

        walletService.updateBalance(userId, newBalance);

        Transaction transaction = new Transaction("Deposit", dto.getAmount(), user, null);
        transactionRepository.save(transaction);

        // Invalidate cache after deposit
        redis.delete(TRANSACTIONS_CACHE_KEY_PREFIX + userId);
    }

    // Withdraw method
    public void withdraw(Long userId, TransactionAmountDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        double currentBalance = walletService.getBalance(userId);
        if (currentBalance < dto.getAmount()) {
            throw new RuntimeException("Insufficient funds.");
        }

        double newBalance = currentBalance - dto.getAmount();

        walletService.updateBalance(userId, newBalance);

        Transaction transaction = new Transaction("Withdraw", dto.getAmount(), user, null);
        transactionRepository.save(transaction);

        // Invalidate cache after withdraw
        redis.delete(TRANSACTIONS_CACHE_KEY_PREFIX + userId);
    }

    // Transfer method with distributed lock
    @DistributedLock(keyPrefix = "lockTransfer", keyIdentifierExpression = "#senderId", leaseTime = 10, timeUnit = TimeUnit.SECONDS)
    public void transfer(Long senderId, TransferTransactionDTO dto) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        User receiver = userRepository.findByMobileNumber(dto.getReceiverMobileNumber())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        if (sender.equals(receiver)) {
            throw new RuntimeException("Cannot transfer money to yourself.");
        }

        double senderBalance = walletService.getBalance(senderId);
        if (senderBalance < dto.getAmount()) {
            throw new RuntimeException("Sender has insufficient funds.");
        }

        double receiverBalance = walletService.getBalance(receiver.getId());

        walletService.updateBalance(senderId, senderBalance - dto.getAmount());
        walletService.updateBalance(receiver.getId(), receiverBalance + dto.getAmount());

        Transaction transaction = new Transaction("Transfer", dto.getAmount(), sender, receiver);
        transactionRepository.save(transaction);

        // Invalidate cache for both sender and receiver
        redis.delete(TRANSACTIONS_CACHE_KEY_PREFIX + senderId);
        redis.delete(TRANSACTIONS_CACHE_KEY_PREFIX + receiver.getId());
    }

    // Get transaction history with caching
    public List<TransactionHistoryDTO> getTransactionsByUserId(Long userId) {
        String cacheKey = TRANSACTIONS_CACHE_KEY_PREFIX + userId;

        try {
            String cachedJson = redis.get(cacheKey);
            if (cachedJson != null) {
                return objectMapper.readValue(cachedJson, new TypeReference<List<TransactionHistoryDTO>>() {});
            }
        } catch (Exception e) {
            // Log error and proceed to DB query
            System.err.println("Failed to read transactions from cache: " + e.getMessage());
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Transaction> transactions = transactionRepository.findBySenderOrReceiver(user, user);

        List<TransactionHistoryDTO> history = transactions.stream()
                .map(tx -> new TransactionHistoryDTO(
                        tx.getType(),
                        tx.getAmount(),
                        tx.getSender() != null ? tx.getSender().getName() : null,
                        tx.getReceiver() != null ? tx.getReceiver().getName() : null,
                        tx.getTransactionDate()
                ))
                .collect(Collectors.toList());

        try {
            String json = objectMapper.writeValueAsString(history);
            redis.set(cacheKey, json, CACHE_TTL);
        } catch (Exception e) {
            System.err.println("Failed to write transactions to cache: " + e.getMessage());
        }

        return history;
    }
}

