package com.example.gelda.transaction.repository;

import com.example.gelda.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import  com.example.gelda.user.entity.User;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySenderOrReceiver(User sender, User receiver);
}
