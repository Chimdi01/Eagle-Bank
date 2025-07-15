package org.banking.service.model;

import java.util.List;

/**
 * Response model for listing multiple transactions for an account.
 */
public class ListTransactionsResponse {
    private List<TransactionResponse> transactions;

    /**
     * Default constructor.
     */
    public ListTransactionsResponse() {}

    /**
     * Gets the list of transactions.
     * @return the list of transactions
     */
    public List<TransactionResponse> getTransactions() {
        return transactions;
    }

    /**
     * Sets the list of transactions.
     * @param transactions the list of transactions
     */
    public void setTransactions(List<TransactionResponse> transactions) {
        this.transactions = transactions;
    }
} 