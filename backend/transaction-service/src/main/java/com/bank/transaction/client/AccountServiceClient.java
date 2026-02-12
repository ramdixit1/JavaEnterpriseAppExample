package com.bank.transaction.client;

import com.bank.common.dto.AccountDtos.BalanceUpdateRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * HTTP client used by transaction-service to call account-service.
 */
@Component
public class AccountServiceClient {

    private final RestTemplate restTemplate;
    private final String accountServiceUrl;

    public AccountServiceClient(RestTemplate restTemplate,
                                @Value("${clients.account-service.url}") String accountServiceUrl) {
        this.restTemplate = restTemplate;
        this.accountServiceUrl = accountServiceUrl;
    }

    /**
     * Sends signed amount to account-service to update available balance.
     */
    public void updateBalance(Long accountId, Double signedAmount) {
        BalanceUpdateRequest payload = new BalanceUpdateRequest(accountId, signedAmount);
        HttpEntity<BalanceUpdateRequest> request = new HttpEntity<>(payload);
        restTemplate.exchange(accountServiceUrl + "/api/accounts/balance", HttpMethod.PATCH, request, Void.class);
    }
}
