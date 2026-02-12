package com.bank.transaction.repository;

import com.bank.transaction.entity.BankTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Data access layer for transaction records.
 */
public interface TransactionRepository extends JpaRepository<BankTransaction, Long> {

    /**
     * Returns all transactions for one account to display account statement.
     */
    List<BankTransaction> findByAccountId(Long accountId);
}
