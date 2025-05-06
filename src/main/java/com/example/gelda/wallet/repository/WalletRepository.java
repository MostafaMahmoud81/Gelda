package com.example.gelda.wallet.repository;

import com.example.gelda.wallet.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Wallet findByUserId(Long userId); // Find wallet by user ID
}
