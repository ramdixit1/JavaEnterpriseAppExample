package com.bank.common.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Holder class for transaction-related DTO records shared by frontend and services.
 */
public final class TransactionDtos {

    /**
     * Private constructor blocks instantiation because this class is used as a DTO namespace.
     */
    private TransactionDtos() {
    }

    /**
     * Request payload for creating a transaction in transaction-service.
     *
     * @param accountId   linked account identifier.
     * @param type        DEBIT or CREDIT.
     * @param amount      transaction amount that must be positive.
     * @param description free-text reason shown in statements.
     */
    public record CreateTransactionRequest(
            @NotNull(message = "Account id is required") Long accountId,
            @NotBlank(message = "Transaction type is required") String type,
            @NotNull(message = "Amount is required") @Positive(message = "Amount must be positive") Double amount,
            @NotBlank(message = "Description is required") String description) {
    }

    /**
     * Response payload returned after transaction creation.
     *
     * @param id persisted transaction identifier.
     * @param accountId related account.
     * @param type transaction type.
     * @param amount absolute amount.
     * @param description reason text.
     */
    public record TransactionResponse(Long id, Long accountId, String type, Double amount, String description) {
    }
}
