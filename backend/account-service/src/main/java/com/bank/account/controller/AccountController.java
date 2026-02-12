package com.bank.account.controller;

import com.bank.account.service.AccountService;
import com.bank.common.dto.AccountDtos.AccountResponse;
import com.bank.common.dto.AccountDtos.BalanceUpdateRequest;
import com.bank.common.dto.AccountDtos.CreateAccountRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller exposing account endpoints to gateway and internal services.
 */
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Creates a new account.
     * Called by frontend via gateway route /accounts.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponse createAccount(@Valid @RequestBody CreateAccountRequest request) {
        return accountService.createAccount(request);
    }

    /**
     * Returns all accounts for account listing page.
     * Called by frontend via gateway route /accounts.
     */
    @GetMapping
    public List<AccountResponse> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    /**
     * Updates account balance.
     * Called by transaction-service internal REST client after transaction creation.
     */
    @PatchMapping("/balance")
    public AccountResponse updateBalance(@Valid @RequestBody BalanceUpdateRequest request) {
        return accountService.updateBalance(request);
    }
}
