package com.bank.account.service;

import com.bank.account.entity.Account;
import com.bank.account.repository.AccountRepository;
import com.bank.common.dto.AccountDtos.AccountResponse;
import com.bank.common.dto.AccountDtos.BalanceUpdateRequest;
import com.bank.common.dto.AccountDtos.CreateAccountRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Business service for account workflows.
 * This module is called by AccountController and also indirectly by transaction-service via REST API.
 */
@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Creates a new account and persists it.
     * Called by AccountController#createAccount.
     */
    public AccountResponse createAccount(CreateAccountRequest request) {
        Account account = new Account();
        account.setCustomerName(request.customerName());
        account.setAccountType(request.accountType());
        account.setBalance(request.openingBalance());

        Account saved = accountRepository.save(account);
        return toResponse(saved);
    }

    /**
     * Reads all accounts.
     * Called by AccountController#getAllAccounts.
     */
    public List<AccountResponse> getAllAccounts() {
        return accountRepository.findAll().stream().map(this::toResponse).toList();
    }

    /**
     * Applies credit/debit amount to an account balance in one database transaction.
     * Called by AccountController#updateBalance which is invoked by transaction-service.
     */
    @Transactional
    public AccountResponse updateBalance(BalanceUpdateRequest request) {
        Account account = accountRepository.findById(request.accountId())
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + request.accountId()));

        double newBalance = account.getBalance() + request.amount();
        if (newBalance < 0) {
            throw new IllegalArgumentException("Insufficient funds for account: " + request.accountId());
        }

        account.setBalance(newBalance);
        return toResponse(account);
    }

    /**
     * Converts internal entity model to API-safe response DTO.
     */
    private AccountResponse toResponse(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getCustomerName(),
                account.getAccountType(),
                account.getBalance()
        );
    }
}
