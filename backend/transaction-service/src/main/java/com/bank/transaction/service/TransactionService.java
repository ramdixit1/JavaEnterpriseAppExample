package com.bank.transaction.service;

import com.bank.common.dto.TransactionDtos.CreateTransactionRequest;
import com.bank.common.dto.TransactionDtos.TransactionResponse;
import com.bank.transaction.client.AccountServiceClient;
import com.bank.transaction.entity.BankTransaction;
import com.bank.transaction.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Business orchestration for transaction operations.
 * Coordinates database persistence and cross-service balance update.
 */
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountServiceClient accountServiceClient;

    public TransactionService(TransactionRepository transactionRepository, AccountServiceClient accountServiceClient) {
        this.transactionRepository = transactionRepository;
        this.accountServiceClient = accountServiceClient;
    }

    /**
     * Creates transaction and updates account balance.
     * Called by TransactionController#createTransaction.
     */
    @Transactional
    public TransactionResponse createTransaction(CreateTransactionRequest request) {
        double signedAmount = resolveSignedAmount(request.type(), request.amount());

        BankTransaction transaction = new BankTransaction();
        transaction.setAccountId(request.accountId());
        transaction.setType(request.type().toUpperCase());
        transaction.setAmount(request.amount());
        transaction.setDescription(request.description());

        BankTransaction saved = transactionRepository.save(transaction);

        // Calls account-service after transaction save to apply debit/credit.
        accountServiceClient.updateBalance(request.accountId(), signedAmount);

        return toResponse(saved);
    }

    /**
     * Reads account statement by account id.
     * Called by TransactionController#getTransactionsByAccount.
     */
    public List<TransactionResponse> getTransactionsByAccount(Long accountId) {
        return transactionRepository.findByAccountId(accountId).stream().map(this::toResponse).toList();
    }

    /**
     * Converts type + amount into signed numeric value for account balance updates.
     */
    private double resolveSignedAmount(String type, Double amount) {
        String upperType = type.toUpperCase();
        if ("CREDIT".equals(upperType)) {
            return amount;
        }
        if ("DEBIT".equals(upperType)) {
            return -amount;
        }
        throw new IllegalArgumentException("Transaction type must be CREDIT or DEBIT");
    }

    /**
     * Converts entity into response DTO.
     */
    private TransactionResponse toResponse(BankTransaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getAccountId(),
                transaction.getType(),
                transaction.getAmount(),
                transaction.getDescription()
        );
    }
}
